package timywimy.repository.common;

import org.hibernate.Criteria;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractEntityRepository<T extends BaseEntity> implements EntityRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    protected CriteriaBuilder builder;

    @PostConstruct
    private void initCriteriaBuilder() {
        this.builder = entityManager.getCriteriaBuilder();
    }

    protected void assertGet(UUID entityId) {
        RequestUtil.validateEmptyField(RepositoryException.class, entityId, "entityId");
    }

    protected T get(Class<T> entityClass, UUID entityId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot).where(getIdExpression(queryRoot, entityId));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }

    protected T get(Class<T> entityClass, UUID entityId,Set<String> fetchParameters) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaQuery<T> criteria = builder.createQuery(entityClass).distinct(true);
        Root<T> queryRoot = criteria.from(entityClass);
        for(String parameter:fetchParameters){
            queryRoot.fetch(parameter,JoinType.LEFT);
        }
        criteria.select(queryRoot).where(getIdExpression(queryRoot, entityId));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }

    protected void assertSave(T entity, UUID userId) {
        RequestUtil.validateEmptyFields(RepositoryException.class,
                new PairFieldName<>(entity, "entity"), new PairFieldName<>(userId, "user"));
    }

    protected T save(Class<T> entityClass, T entity, UUID userId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");
        if (entity.isNew()) {
            entity.setCreatedBy(userId);
            entityManager.persist(entity);
            return entity;
        } else {
            entity.setUpdatedBy(userId);
            return entityManager.merge(entity);
        }
    }

    protected void assertDelete(UUID entityId, UUID userId) {
        RequestUtil.validateEmptyFields(RepositoryException.class,
                new PairFieldName<>(entityId, "entity id"), new PairFieldName<>(userId, "user id"));
    }

    protected boolean delete(Class<T> entityClass, UUID entityId, UUID userId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaDelete<T> criteria = builder.createCriteriaDelete(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.where(getIdExpression(queryRoot, entityId));

        return entityManager.createQuery(criteria).executeUpdate() != 0;
    }

    protected List<T> getAll(Class<T> entityClass) {
        Assert.notNull(entityClass, "entity class should be provided to construct get query");

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot);

        return entityManager.createQuery(criteria).getResultList();
    }

    protected T getSingleFromResultList(List<T> resultList) {
        if (resultList.size() == 0) {
            return null;
        } else if (resultList.size() == 1) {
            return resultList.get(0);
        } else {
            throw new RepositoryException(ErrorCode.INTERNAL_REPOSITORY, "Multiple results by entityId returned");
        }
    }

    protected Expression<Boolean> getIdExpression(Root<T> queryRoot, UUID entityId) {
        return builder.equal(queryRoot.get("id"), entityId);
    }

//    protected Expression<Boolean> getDeletedTsExpression(Root<T> queryRoot, Expression<Boolean> expression) {
//        if (expression == null) {
//            return builder.isNull(queryRoot.get("deletedTs"));
//        } else {
//            return builder.and(
//                    expression,
//                    builder.isNull(queryRoot.get("deletedTs")));
//        }
//    }
}
