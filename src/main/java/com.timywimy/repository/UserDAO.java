package com.timywimy.repository;

import com.timywimy.model.security.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface UserDAO extends AbstractDAO<User> {

    User getByEmail(String email);

    boolean ban(User ban, UUID bannedBy, ZonedDateTime bannedTill);

    boolean unBan(User bannedUser, UUID unbannedBy);

}
