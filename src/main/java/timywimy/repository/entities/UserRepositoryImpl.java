package timywimy.repository.entities;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.security.UserImpl;

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
    public UserImpl save(UserImpl entity, UUID updatedBy) {
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

    //todo retrieve names from some kind of model,not strings
    @Override
    public UserImpl get(UUID id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserImpl> criteria = builder.createQuery(UserImpl.class);
        Root<UserImpl> userRoot = criteria.from(UserImpl.class);

        criteria.select(userRoot).where(
                builder.and(
                        builder.equal(userRoot.get("id"), id),
                        builder.isNotNull(userRoot.get("deleted_ts"))));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID deletedBy) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserImpl> criteria = builder.createCriteriaUpdate(UserImpl.class);
        Root<UserImpl> userRoot = criteria.from(UserImpl.class);
        criteria.set(userRoot.get("deleted_by"), deletedBy).
                set(userRoot.get("deleted_ts"), ZonedDateTime.now()).
                where(builder.and(
                        builder.equal(userRoot.get("id"), id),
                        builder.isNotNull(userRoot.get("deleted_ts"))));
        return entityManager.createQuery(criteria).executeUpdate() != 0;
    }

    @Override
    public List<UserImpl> getAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserImpl> criteria = builder.createQuery(UserImpl.class);
        Root<UserImpl> userRoot = criteria.from(UserImpl.class);
        criteria.select(userRoot).where(
                builder.isNotNull(userRoot.get("deleted_ts")))
                .orderBy(builder.asc(userRoot.get("name")));
        return entityManager.createQuery(criteria)
                .getResultList();
    }


    @Override
    public UserImpl getByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserImpl> criteria = builder.createQuery(UserImpl.class);
        Root<UserImpl> userRoot = criteria.from(UserImpl.class);
        criteria.select(userRoot).where(
                builder.and(
                        builder.equal(userRoot.get("email"), email),
                        builder.isNotNull(userRoot.get("deleted_ts"))));
        return entityManager.createQuery(criteria).getSingleResult();
    }
}
