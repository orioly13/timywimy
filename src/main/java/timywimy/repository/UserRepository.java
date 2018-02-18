package timywimy.repository;

import timywimy.model.security.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface UserRepository extends AbstractRepository<User> {

    User getByEmail(String email);

    boolean ban(User ban, UUID bannedBy, ZonedDateTime bannedTill);

    boolean unBan(User bannedUser, UUID unbannedBy);

}
