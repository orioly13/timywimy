package com.timywimy.service;

import com.timywimy.model.security.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface UserService extends AbstractService<User> {

    User getByEmail(String email, UUID user);

    boolean ban(UUID idToBan, UUID bannedBy, ZonedDateTime bannedTill);

    boolean unBan(UUID bannedId, UUID bannedBy);

}
