package timywimy.service.entities;

import timywimy.model.security.User;

import java.util.UUID;

public interface UserService extends EntityService<User> {

    User getByEmail(String email, UUID session);

//    boolean ban(UUID idToBan, User bannedBy, ZonedDateTime bannedTill);
//
//    boolean unBan(UUID bannedId, User bannedBy);

}
