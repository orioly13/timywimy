package timywimy.repository.entities;

import timywimy.model.security.UserImpl;

public interface UserRepository extends EntityRepository<UserImpl> {

    UserImpl getByEmail(String email);

}
