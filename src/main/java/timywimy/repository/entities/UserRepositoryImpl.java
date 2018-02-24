package timywimy.repository.entities;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.security.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User save(User entity, UUID updatedBy) {
        if (entity.isNew()) {
            entity.setCreatedBy(updatedBy);
            entityManager.persist(entity);
            return entity;
        } else {
            //todo add check on deleted_ts
            entity.setUpdatedBy(updatedBy);
            return entityManager.merge(entity);
        }
    }

    @Override
    public User get(UUID id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);

        criteria.select(userRoot).where(
                builder.and(
                        builder.equal(userRoot.get("id"), id),
                        builder.isNull(userRoot.get("deletedTs"))));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID deletedBy) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> criteria = builder.createCriteriaUpdate(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.set(userRoot.get("deletedBy"), deletedBy).
                set(userRoot.get("deletedTs"), ZonedDateTime.now()).
                where(builder.and(
                        builder.equal(userRoot.get("id"), id),
                        builder.isNotNull(userRoot.get("deletedTs"))));
        return entityManager.createQuery(criteria).executeUpdate() != 0;
    }

    @Override
    public List<User> getAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).where(
                builder.isNotNull(userRoot.get("deletedTs")))
                .orderBy(builder.asc(userRoot.get("name")));
        return entityManager.createQuery(criteria)
                .getResultList();
    }


    @Override
    public User getByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot).where(
                builder.and(
                        builder.equal(userRoot.get("email"), email),
                        builder.isNull(userRoot.get("deletedTs"))));
        List<User> resultList = entityManager.createQuery(criteria).getResultList();
        if (resultList.size() == 1) {
            return resultList.get(0);
        } else if (resultList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("multiple entries found");
        }
    }
}
