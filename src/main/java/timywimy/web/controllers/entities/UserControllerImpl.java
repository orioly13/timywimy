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
    public User getByEmail(String email, UUID user) {
        return null;
    }

    @Override
    public boolean create(User entity, UUID createdBy) {
        return false;
    }

    @Override
    public User get(UUID id, UUID user) {
        return null;
    }

    @Override
    public User update(User entity, UUID updatedBy) {
        return null;
    }

    @Override
    public boolean delete(User entity, UUID deletedBy) {
        return false;
    }

    @Override
    public List<User> getAll(UUID user) {
        return null;
    }
}