package timywimy.web.controllers.entities;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.service.entities.UserService;

import java.util.List;
import java.util.UUID;

@Controller
public class UserControllerImpl extends AbstractEntityController<User> implements UserController {

    @Autowired
    public UserControllerImpl(UserService service) {
        super(service,
                LoggerFactory.getLogger(UserControllerImpl.class));
    }

    @Override
    public User get(UUID id, UUID session) {
        return service.get(id, session);
    }

    @Override
    public User save(User entity, UUID session) {
        return service.save(entity, session);
    }

    @Override
    public boolean delete(UUID id, UUID session) {
        return service.delete(id, session);
    }

    @Override
    public List<User> getAll(UUID session) {
        return service.getAll(session);
    }

    @Override
    public User getByEmail(String email, UUID session) {
        Assert.notNull(email, "entity class must not be null");
        Assert.notNull(session, "user session must not be null");
        return ((UserService) service).getByEmail(email, session);
    }

}