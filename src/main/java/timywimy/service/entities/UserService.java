package timywimy.service.entities;


import timywimy.web.dto.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface UserService extends EntityService<User, timywimy.model.security.User> {

    User getByEmail(String email, UUID session);

    boolean ban(UUID idToBan, UUID session, ZonedDateTime bannedTill);

    boolean unBan(UUID bannedId, UUID session);

}
