package com.timywimy.repository;

import com.timywimy.model.security.User;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDAOImpl implements UserDAO {

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public boolean ban(User ban, UUID bannedBy, ZonedDateTime bannedTill) {
        return false;
    }

    @Override
    public boolean unBan(User bannedUser, UUID unbannedBy) {
        return false;
    }

    @Override
    public boolean create(User entity) {
        return false;
    }

    @Override
    public User get(UUID id) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
