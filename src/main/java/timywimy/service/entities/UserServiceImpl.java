package timywimy.service.entities;

import org.springframework.stereotype.Service;
import timywimy.model.security.converters.Role;
import timywimy.repository.UserRepository;
import timywimy.service.RestService;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.User;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractEntityWithRightsService<User, timywimy.model.security.User> implements UserService {

    public UserServiceImpl(RestService restService, UserRepository repository) {
        super(restService, repository);
    }

    @PostConstruct
    private void initRoles() {
        super.setRoles(Collections.singletonList(Role.ADMIN));
    }

    @Override
    public User get(UUID entityId, UUID userSession) {
        timywimy.model.security.User entity = getEntity(entityId, userSession);
        return convertEntityToDTO(entity);
    }

    @Override
    public User save(User dto, UUID userSession) {
        throw new ServiceException(ErrorCode.REQUEST_VALIDATION_OPERATION_RESTRICTED);
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        throw new ServiceException(ErrorCode.REQUEST_VALIDATION_OPERATION_RESTRICTED);
    }

    @Override
    public List<User> getAll(UUID userSession) {
        List<timywimy.model.security.User> allEntities = getAllEntities(userSession);
        List<User> result = new ArrayList<>(allEntities.size());
        for (timywimy.model.security.User entity : allEntities) {
            result.add(convertEntityToDTO(entity));
        }
        return result;
    }

    @Override
    public User getByEmail(String email, UUID session) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(email, "email"),
                new PairFieldName<>(session, "session"));
        timywimy.model.security.User userBySession = getUserBySession(session);
        assertUserRole(userBySession.getRole());
        timywimy.model.security.User entity = ((UserRepository) repository).getByEmail(email);
        return convertEntityToDTO(entity);
    }

    @Override
    public boolean ban(UUID idToBan, UUID session, ZonedDateTime bannedTill) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(idToBan, "id to ban"),
                new PairFieldName<>(session, "session"));
        timywimy.model.security.User userBySession = getUserBySession(session);
        assertUserRole(userBySession.getRole());

        timywimy.model.security.User user = repository.get(idToBan);
        if (user == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
        }
        user.setBanned(true);
        user.setBannedBy(userBySession);
        user.setBannedTill(bannedTill);
        return repository.save(user, userBySession.getId()) != null;
    }

    @Override
    public boolean unBan(UUID bannedId, UUID session) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(bannedId, "banned id"),
                new PairFieldName<>(session, "session"));
        timywimy.model.security.User userBySession = getUserBySession(session);
        assertUserRole(userBySession.getRole());

        timywimy.model.security.User user = repository.get(bannedId);
        if (user == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
        }
        user.setBanned(false);
        user.setBannedBy(userBySession);
        user.setBannedTill(null);
        return repository.save(user, userBySession.getId()) != null;
    }

    private User convertEntityToDTO(timywimy.model.security.User user) {
        if (user == null)
            return null;
        User res = new User();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setBanned(user.isBanned());
        res.setBannedTill(user.getBannedTill());
        return res;
    }

}