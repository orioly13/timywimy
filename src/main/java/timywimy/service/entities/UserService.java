package timywimy.service.entities;

import timywimy.model.security.UserImpl;

import java.util.UUID;

public interface UserService extends EntityService<UserImpl> {

    UserImpl getByEmail(String email, UUID session);

//    boolean ban(UUID idToBan, User bannedBy, ZonedDateTime bannedTill);
//
//    boolean unBan(UUID bannedId, User bannedBy);

}
