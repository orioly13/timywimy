package timywimy.service;

import timywimy.model.security.User;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public User getByEmail(String email, UUID user) {
        return null;
    }

    @Override
    public boolean ban(UUID idToBan, UUID bannedBy, ZonedDateTime bannedTill) {
        return false;
    }

    @Override
    public boolean unBan(UUID bannedId, UUID bannedBy) {
        return false;
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