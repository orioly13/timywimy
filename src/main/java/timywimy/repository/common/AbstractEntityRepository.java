package timywimy.repository.common;

import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityRepository<T extends BaseEntity> implements EntityRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    protected CriteriaBuilder builder;

    @PostConstruct
    private void initCriteriaBuilder() {
        this.builder = entityManager.getCriteriaBuilder();
    }


    protected T get(Class<T> entityClass, UUID entityId) {
        Assert.notNull(entityClass, "entity class should be provided to construct get query");
        Assert.notNull(entityId, "entityId should be provided");

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot).where(getIdExpression(queryRoot, entityId));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }

    protected T save(Class<T> entityClass, T entity, UUID userId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");
        Assert.notNull(entity, "entity should be provided");
        Assert.notNull(userId, "userId entityId should be provided");
        Assert.isNull(entity.getDeletedTs(), "cannot save with deletedTs (use delete method)");

        if (entity.isNew()) {
            entity.setCreatedBy(userId);
            entityManager.persist(entity);
            return entity;
        } else {
            //find entity and check if it's deleted (if not- update)
            T foundEntity = entityManager.find(entityClass, entity.getId());
            if (foundEntity == null || foundEntity.getDeletedTs() != null) {
                return null;
            }
            entity.setUpdatedBy(userId);
            return entityManager.merge(entity);
        }
    }

    protected boolean delete(Class<T> entityClass, UUID entityId, UUID userId) {
        Assert.notNull(entityClass, "entity class should be provided to construct get query");
        Assert.notNull(entityId, "entity should be provided");
        Assert.notNull(userId, "userId entityId should be provided");

        CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.set(queryRoot.get("deletedBy"), userId).
                set(queryRoot.get("updatedBy"), userId).
                set(queryRoot.get("deletedTs"), ZonedDateTime.now()).
                where(getIdExpression(queryRoot, entityId));

        return entityManager.createQuery(criteria).executeUpdate() != 0;
    }

    protected List<T> getAll(Class<T> entityClass) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot).where(getDeletedTsExpression(queryRoot, null));

        return entityManager.createQuery(criteria).getResultList();
    }

    protected T getSingleFromResultList(List<T> resultList) {
        if (resultList.size() == 0) {
            return null;
        } else if (resultList.size() == 1) {
            return resultList.get(0);
        } else {
            throw new RuntimeException("Multiple results by entityId returned");
        }
    }

    protected Expression<Boolean> getIdExpression(Root<T> queryRoot, UUID entityId) {
        return getDeletedTsExpression(queryRoot, builder.equal(queryRoot.get("id"), entityId));
    }

    protected Expression<Boolean> getDeletedTsExpression(Root<T> queryRoot, Expression<Boolean> expression) {
        if (expression == null) {
            return builder.isNull(queryRoot.get("deletedTs"));
        } else {
            return builder.and(
                    expression,
                    builder.isNull(queryRoot.get("deletedTs")));
        }
    }
}
