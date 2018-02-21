package timywimy.web.controllers.entities;

import timywimy.model.security.User;

import java.util.UUID;

public interface UserController extends AbstractController<User> {

    User getByEmail(String email, UUID session);
//
//    boolean ban(UUID idToBan, UUID bannedBy, ZonedDateTime bannedTill);
//
//    boolean unBan(UUID bannedId, UUID bannedBy);

}
