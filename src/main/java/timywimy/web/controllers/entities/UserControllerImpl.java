package timywimy.web.controllers.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import timywimy.model.security.User;
import timywimy.service.entities.UserService;

import java.util.List;
import java.util.UUID;

@Controller
public class UserControllerImpl implements UserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final UserService service;

    @Autowired
    public UserControllerImpl(UserService service) {
        this.service = service;
    }

    @Override
    public User getByEmail(String email, UUID session) {
        return service.getByEmail(email, session);
    }

    @Override
    public User create(User entity, UUID session) {
        return service.create(entity, session);
    }

    @Override
    public User get(UUID id, UUID session) {
        return service.get(id, session);
    }

    @Override
    public User update(User entity, UUID session) {
        return service.update(entity, session);
    }

    @Override
    public boolean delete(UUID id, UUID session) {
        return service.delete(id, session);
    }

    @Override
    public List<User> getAll(UUID session) {
        return service.getAll(session);
    }
}