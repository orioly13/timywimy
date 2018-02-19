package timywimy.repository.entities;

import timywimy.model.security.User;

public interface UserRepository extends EntityRepository<User> {

    User getByEmail(String email);

}
