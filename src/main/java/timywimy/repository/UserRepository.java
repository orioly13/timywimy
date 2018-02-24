package timywimy.repository;

import timywimy.model.security.User;
import timywimy.repository.common.EntityRepository;

public interface UserRepository extends EntityRepository<User> {

    User getByEmail(String email);

}
