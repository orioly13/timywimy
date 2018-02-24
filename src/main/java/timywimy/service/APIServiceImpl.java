package timywimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.UserRepository;
import timywimy.service.dto.UserSession;
import timywimy.util.StringUtil;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class APIServiceImpl implements APIService {

    private final UserRepository userRepository;
    private Map<UUID, UserSession> sessions;
    private User apiUser;

    @Autowired
    public APIServiceImpl(UserRepository userRepository) {
        Assert.notNull(userRepository, "UserRepository should be provided");
        this.userRepository = userRepository;
    }

    //PostConstruct annotation applies AFTER  Autowired
    @PostConstruct
    private void init() {
        this.sessions = new HashMap<>();
        this.apiUser = userRepository.get(UUID.fromString("3088b1fc-43c2-4951-8b78-1f56261c16ca"));
    }

    @Override
    public UUID register(timywimy.web.dto.User userDTO) {
        Assert.notNull(userDTO, "user should be provided");
        if (StringUtil.isEmpty(userDTO.getEmail()) ||
                StringUtil.isEmpty(userDTO.getPassword()) ||
                StringUtil.isEmpty(userDTO.getName())) {
            throw new RuntimeException("user with email, pass and name should be provided");
        }
        if (userRepository.getByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("user already registered");
        }
        User saved = userRepository.save(createFromUserDTO(userDTO), apiUser.getId());
        if (saved == null) {
            throw new RuntimeException("failed to register user");
        }
        UUID session = UUID.randomUUID();
        sessions.put(session, new UserSession(session, saved));
        return session;
    }

    @Override
    public UUID openSession(timywimy.web.dto.User userDTO) {
        Assert.notNull(userDTO, "user should be provided");
        if (StringUtil.isEmpty(userDTO.getEmail()) || StringUtil.isEmpty(userDTO.getPassword())) {
            throw new RuntimeException("user with email,pass should be provided");
        }
        User byEmail = userRepository.getByEmail(userDTO.getEmail());
        if (byEmail == null || !byEmail.getPassword().equals(userDTO.getPassword())) {
            throw new RuntimeException("user with email and pass not found");
        }
        UUID session = UUID.randomUUID();
        sessions.put(session, new UserSession(session, byEmail));
        return session;
    }

    @Override
    public boolean closeSession(UUID sessionId) {
        Assert.notNull(sessionId, "session should be provided");
        sessions.remove(sessionId);
        return true;
    }

    @Override
    public User getUserBySession(UUID sessionId) {
        Assert.notNull(sessionId, "session should be provided");
        UserSession entry = sessions.get(sessionId);
        if (entry.getExpiryDate().isAfter(ZonedDateTime.now())) {
            sessions.remove(sessionId);
            return null;
        }
        return entry.getUser();
    }

    private User createFromUserDTO(timywimy.web.dto.User dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setRole(Role.USER);
        return user;
    }
}
