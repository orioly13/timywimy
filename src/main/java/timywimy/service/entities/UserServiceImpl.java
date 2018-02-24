package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.entities.UserRepository;
import timywimy.service.APIService;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractEntityService<User> implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository, APIService apiService) {
        super(apiService);
        Assert.notNull(repository, "UserRepository should be provided");
        this.repository = repository;
    }

    @Override
    public User create(User entity, UUID userSession) {
        Assert.notNull(entity, "entity must not be null");
        Assert.notNull(userSession, "user must not be null");
        return repository.save(entity, getUserBySession(userSession).getId());
    }

    @Override
    public User update(User entity, UUID userSession) {
        Assert.notNull(entity, "id must not be null");
        Assert.notNull(userSession, "user must not be null");
        return repository.save(entity, getUserBySession(userSession).getId());
    }

    @Override
    public User get(UUID id, UUID userSession) {
        Assert.notNull(id, "entity must not be null");
        Assert.notNull(userSession, "user must not be null");
        getUserBySession(userSession);
        return repository.get(id);
    }

    @Override
    public boolean delete(UUID id, UUID userSession) {
        Assert.notNull(id, "entity must not be null");
        Assert.notNull(userSession, "user must not be null");
        return repository.delete(id, getUserBySession(userSession).getId());
    }

    @Override
    public List<User> getAll(UUID userSession) {
        getUserBySession(userSession);
        return repository.getAll();
    }

    private User getUserBySession(UUID session) {
        User userBySession = apiService.getUserBySession(session);
        if (!Role.ADMIN.equals(userBySession.getRole())) {
            throw new RuntimeException("not enough rights");
        }
        return userBySession;
    }

    @Override
    public User getByEmail(String email, UUID session) {
        getUserBySession(session);
        return repository.getByEmail(email);
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