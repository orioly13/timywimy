package timywimy.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEntityRepository;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl extends AbstractEntityRepository<User> implements UserRepository {

    @Override
    public User get(UUID id) {
        assertGet(id);
        return get(User.class, id);
    }

    @Override
    public User get(UUID id, Set<String> properties) {
        assertGet(id);
        return get(User.class, id, properties);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ConstraintViolationException.class})
    public User save(User entity, UUID userId) {
        assertSave(entity, userId);
        RequestUtil.validateEmptyFields(RepositoryException.class,
                new PairFieldName<>(entity.getEmail(), "user email"),
                new PairFieldName<>(entity.getPassword(), "user password"),
                new PairFieldName<>(entity.getName(), "user name"));
        User byEmail = getByEmail(entity.getEmail());
        if (byEmail != null && !byEmail.getId().equals(entity.getId())) {
            throw new RepositoryException(ErrorCode.USER_ALREADY_REGISTERED);
        }
        return super.save(entity, userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ConstraintViolationException.class})
    public boolean delete(UUID id) {
        assertDelete(id);

        return delete(User.class, id);
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }

    @Override
    public List<User> getAll() {
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).
                orderBy(builder.asc(userRoot.get("name")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public User getByEmail(String email) {
        RequestUtil.validateEmptyField(RepositoryException.class, email, "email");

        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).where(builder.equal(userRoot.get("email"), email));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }
}
