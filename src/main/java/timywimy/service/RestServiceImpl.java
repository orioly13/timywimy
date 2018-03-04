package timywimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.converters.Role;
import timywimy.repository.UserRepository;
import timywimy.service.dto.UserSession;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.StringUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.Session;
import timywimy.web.dto.User;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class RestServiceImpl implements RestService {

    private final UserRepository userRepository;
    private final Long expireMinutes;
    private final Pattern passwordPattern;
    private final Pattern emailPattern;
    private Map<UUID, UserSession> sessions;
    private timywimy.model.security.User apiUser;

    @Autowired
    public RestServiceImpl(UserRepository userRepository,
                           @Value("${api.session.expiryMinutes}") Long expireMinutes,
                           @Value("${api.user.passwordPattern.regexp}") String passwordPattern,
                           @Value("${api.user.emailPattern.regexp}") String emailPattern) {
        Assert.notNull(expireMinutes, "ExpireMinutes should be provided");
        Assert.notNull(userRepository, "UserRepository should be provided");
        Assert.notNull(passwordPattern, "PasswordPattern should be provided");
        Assert.notNull(emailPattern, "EmailPattern should be provided");
        this.expireMinutes = expireMinutes;
        this.userRepository = userRepository;
        this.passwordPattern = Pattern.compile(passwordPattern);
        this.emailPattern = Pattern.compile(emailPattern);
    }

    //PostConstruct annotation applies AFTER  Autowired
    @PostConstruct
    private void init() {
        this.sessions = new HashMap<>();
        this.apiUser = userRepository.get(UUID.fromString("3088b1fc-43c2-4951-8b78-1f56261c16ca"));
    }

    private void validateEmailPattern(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "Email is invalid format");
        }
    }

    private void validatePasswordPattern(String password) {
        if (!passwordPattern.matcher(password).matches()) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "password must be at least 6 chars long, " +
                    "contain upper and lower case latin letters, digits and one of the following symbols: @#$%");
        }
    }

    @Override
    public Session register(User user) {
        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(user, "user"));
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(user.getEmail(), "email"),
                new PairFieldName<>(user.getPassword(), "password"),
                new PairFieldName<>(user.getName(), "name"));
        String lowerCaseEmail = user.getEmail().toLowerCase();
        validateEmailPattern(lowerCaseEmail);
        validatePasswordPattern(user.getPassword());

        if (userRepository.getByEmail(lowerCaseEmail) != null) {
            throw new ServiceException(ErrorCode.REGISTER_ALREADY_REGISTERED, String.format("email:%s", user.getEmail()));
        }
        timywimy.model.security.User savedUser = userRepository.save(createFromUserDTO(user), apiUser.getId());
        if (savedUser == null) {
            throw new ServiceException(ErrorCode.REGISTER_FAILED_TO_PERSIST);
        }

        UUID session = UUID.randomUUID();
        UserSession userSession = sessions.put(session, new UserSession(session, savedUser, expireMinutes));
        return new Session(session, userSession.getExpiryDate(), new User(savedUser.getEmail(), savedUser.getName()));
    }

    @Override
    public Session openSession(User user) {
        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(user, "user"));
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(user.getEmail(), "email"),
                new PairFieldName<>(user.getPassword(), "password"));
        String lowerCaseEmail = user.getEmail().toLowerCase();
        validateEmailPattern(lowerCaseEmail);
        validatePasswordPattern(user.getPassword());

        timywimy.model.security.User userByEmail = userRepository.getByEmail(lowerCaseEmail);
        if (userByEmail == null || !userByEmail.getPassword().equals(user.getPassword())) {
            throw new ServiceException(ErrorCode.SESSION_USER_NOT_FOUND, "Please check email and password");
        }

        UUID session = UUID.randomUUID();
        UserSession userSession = sessions.put(session, new UserSession(session, userByEmail, expireMinutes));
        return new Session(session, userSession.getExpiryDate(), new User(userByEmail.getEmail(), userByEmail.getName()));
    }

    @Override
    public boolean closeSession(UUID sessionId) {
        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(sessionId, "session"));
        sessions.remove(sessionId);
        return true;
    }

    @Override
    public User updateProfile(UUID sessionId, User user) {
        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(sessionId, "session"));
        //find session
        timywimy.model.security.User userBySession = getUserBySession(sessionId);
        if (userBySession == null) {
            throw new ServiceException(ErrorCode.SESSION_NOT_FOUND);
        }

        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(user, "user"));
        //email change check
        if (!StringUtil.isEmpty(user.getEmail())) {
            String lowerCaseEmail = user.getEmail().toLowerCase();
            if (!userBySession.getEmail().equals(lowerCaseEmail)) {
                validateEmailPattern(lowerCaseEmail);
                timywimy.model.security.User byEmail = userRepository.getByEmail(lowerCaseEmail);
                if (byEmail != null) {
                    throw new ServiceException(ErrorCode.REGISTER_ALREADY_REGISTERED, String.format("email:%s", user.getEmail()));
                }
                userBySession.setEmail(lowerCaseEmail);
            }
        }
        //password change check
        if (!StringUtil.isEmpty(user.getPassword())) {
            RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(user.getOldPassword(), "old password"));
            if (userBySession.getPassword().equals(user.getOldPassword())) {
                validatePasswordPattern(user.getPassword());
                userBySession.setPassword(user.getPassword());
            } else {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "Old password differs from provided");
            }
        }
        if (!StringUtil.isOnlySpaces(user.getName())) {
            userBySession.setName(user.getName());
        }
        timywimy.model.security.User savedUser = userRepository.save(userBySession, userBySession.getId());
        if (savedUser == null) {
            throw new ServiceException(ErrorCode.REGISTER_FAILED_TO_PERSIST);
        }
        return new User(savedUser.getEmail(), savedUser.getName());
    }

    //internal methods called by other services
    @Override
    public timywimy.model.security.User getUserBySession(UUID sessionId) {
        RequestUtil.validateEmptyFields(ServiceException.class, new PairFieldName<>(sessionId, "session"));
        UserSession entry = sessions.get(sessionId);
        if (entry.getExpiryDate().isAfter(ZonedDateTime.now())) {
            sessions.remove(sessionId);
            return null;
        }
        return entry.getUser();
    }

    private timywimy.model.security.User createFromUserDTO(User dto) {
        timywimy.model.security.User user = new timywimy.model.security.User();
        //to lowercase to prevent changes
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setRole(Role.USER);
        return user;
    }
}