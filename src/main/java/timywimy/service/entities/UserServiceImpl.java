package timywimy.service.entities;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.UserRepository;
import timywimy.service.APIService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractEntityWithRightsService<User> implements UserService {

    public UserServiceImpl(APIService apiService, UserRepository repository) {
        super(apiService, repository);
    }

    @PostConstruct
    private void initRoles() {
        super.setRoles(Collections.singletonList(Role.ADMIN));
    }

    @Override
    public User get(UUID entityId, UUID userSession) {
        return get(User.class, entityId, userSession);
    }

    @Override
    public User save(User entity, UUID userSession) {
        return save(User.class, entity, userSession);
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        return delete(User.class, entityId, userSession);
    }

    @Override
    public List<User> getAll(UUID userSession) {
        return getAll(User.class, userSession);
    }

    @Override
    public User getByEmail(String email, UUID session) {
        Assert.notNull(email, "entity class must not be null");
        Assert.notNull(session, "user session must not be null");
        User userBySession = getUserBySession(session);
        assertUserRole(userBySession.getRole());
        return ((UserRepository) repository).getByEmail(email);
    }

//    @Override
//    public boolean ban(UUID idToBan, User bannedBy, ZonedDateTime bannedTill) {
//        Assert.notNull(idToBan, "id must not be null");
//        Assert.notNull(bannedBy, "bannedBy must not be null");
//     
//        return false;
//    }
//
//    @Override
//    public boolean unBan(UUID bannedId, User updatedBy) {
//        return false;
//    }
}