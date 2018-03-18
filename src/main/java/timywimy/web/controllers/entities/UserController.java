package timywimy.web.controllers.entities;


import timywimy.web.dto.security.User;
import timywimy.web.dto.common.Response;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface UserController extends EntityController<User> {

    Response<User> getByEmail(Integer requestId, String email, UUID session);

    Response<Boolean> ban(Integer requestId, UUID idToBan, UUID bannedBy, String bannedTill);

    Response<Boolean> unBan(Integer requestId, UUID bannedId, UUID unbannedBy);

}
