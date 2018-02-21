package timywimy.web.controllers.entities;

import timywimy.model.security.UserImpl;

import java.util.UUID;

public interface UserController extends AbstractController<UserImpl> {

    UserImpl getByEmail(String email, UUID session);
//
//    boolean ban(UUID idToBan, UUID bannedBy, ZonedDateTime bannedTill);
//
//    boolean unBan(UUID bannedId, UUID bannedBy);

}
