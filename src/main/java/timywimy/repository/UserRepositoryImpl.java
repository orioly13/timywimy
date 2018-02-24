package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEntityRepository;
import timywimy.util.StringUtil;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
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
    @Transactional
    public User save(User entity, UUID userId) {
        assertSave(entity, userId);
        Assert.isTrue(!StringUtil.isEmpty(entity.getName()), "user.name should be provided");
        Assert.isTrue(!StringUtil.isEmpty(entity.getPassword()), "user.password should be provided");
        Assert.isTrue(!StringUtil.isEmpty(entity.getEmail()), "user.email should be provided");
        User byEmail = getByEmail(entity.getEmail());
        Assert.isTrue(byEmail == null || byEmail.getId().equals(entity.getId()), "user with this email already exists");

        return save(User.class, entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID userId) {
        assertDelete(id, userId);

        return delete(User.class, id, userId);
    }

    @Override
    public List<User> getAll() {
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).where(getDeletedTsExpression(userRoot, null))
                .orderBy(builder.asc(userRoot.get("name")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public User getByEmail(String email) {
        Assert.isTrue(!StringUtil.isEmpty(email), "email should be provided");

        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).where(
                getDeletedTsExpression(userRoot, builder.equal(userRoot.get("email"), email)));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }
}
