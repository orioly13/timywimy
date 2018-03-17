package timywimy.repository.common;

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
import java.util.*;

public abstract class AbstractEntityRepository {

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

    protected <E extends BaseEntity> E getBaseEntity(Class<E> entityClass, UUID entityId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaQuery<E> criteria = builder.createQuery(entityClass);
        Root<E> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot).where(getIdExpression(queryRoot, entityId));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }

    protected <E extends BaseEntity> E getBaseEntity(Class<E> entityClass, UUID entityId, Set<String> fetchParameters) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaQuery<E> criteria = builder.createQuery(entityClass).distinct(true);
        Root<E> queryRoot = criteria.from(entityClass);
        for (String parameter : fetchParameters) {
            queryRoot.fetch(parameter, JoinType.LEFT);
        }
        criteria.select(queryRoot).where(getIdExpression(queryRoot, entityId));

        return getSingleFromResultList(entityManager.createQuery(criteria).getResultList());
    }

    protected <E extends BaseEntity> void assertSave(E entity, UUID userId) {
        RequestUtil.validateEmptyFields(RepositoryException.class,
                new PairFieldName<>(entity, "entity"), new PairFieldName<>(userId, "user"));
    }

    protected <E extends BaseEntity> E saveBaseEntity(E entity, UUID userId) {
        if (entity.isNew()) {
            entity.setCreatedBy(userId);
            entityManager.persist(entity);
            return entity;
        } else {
            entity.setUpdatedBy(userId);
            return entityManager.merge(entity);
        }
    }

    protected void assertDelete(UUID entityId) {
        RequestUtil.validateEmptyField(RepositoryException.class, entityId, "entity id");
    }

    protected <E extends BaseEntity> boolean deleteBaseEntity(Class<E> entityClass, UUID entityId) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");

        CriteriaDelete<E> criteria = builder.createCriteriaDelete(entityClass);
        Root<E> queryRoot = criteria.from(entityClass);
        criteria.where(getIdExpression(queryRoot, entityId));

        return entityManager.createQuery(criteria).executeUpdate() != 0;
    }

    protected <E extends BaseEntity> boolean deleteBaseEntity(Class<E> entityClass, E entity) {
        Assert.notNull(entityClass, "entity class should be provided to construct query");
        RequestUtil.validateEmptyField(RepositoryException.class, entity, "entity");
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getId(), "entity id");
        E foundEntity = entityManager.find(entityClass, entity.getId());
        if (foundEntity != null) {
            entityManager.remove(foundEntity);
        } else {
            throw new RepositoryException(ErrorCode.ENTITY_NOT_FOUND);
        }
        return true;
    }

    protected <E extends BaseEntity> List<E> getAllBaseEntities(Class<E> entityClass) {
        Assert.notNull(entityClass, "entity class should be provided to construct get query");

        CriteriaQuery<E> criteria = builder.createQuery(entityClass);
        Root<E> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot);

        return entityManager.createQuery(criteria).getResultList();
    }

    protected <E extends BaseEntity> E getSingleFromResultList(List<E> resultList) {
        if (resultList.size() == 0) {
            return null;
        } else if (resultList.size() == 1) {
            return resultList.get(0);
        } else {
            throw new RepositoryException(ErrorCode.INTERNAL_REPOSITORY, "Multiple results by entityId returned");
        }
    }

    protected <E extends BaseEntity> Expression<Boolean> getIdExpression(Root<E> queryRoot, UUID entityId) {
        return builder.equal(queryRoot.get("id"), entityId);
    }

    //
    protected <E extends BaseEntity> void persistEntity(UUID userId, E toAdd) {
        toAdd.setCreatedBy(userId);
        toAdd.setId(null);
        entityManager.persist(toAdd);
    }

    protected <E extends BaseEntity> boolean isFoundToDelete(E toDelete, List<E> children) {
        boolean foundToDelete = false;
        Iterator<E> iterator = children.iterator();
        while (iterator.hasNext()) {
            E child = iterator.next();
            if (toDelete.getId().equals(child.getId())) {
                foundToDelete = true;
                entityManager.remove(child);
                iterator.remove();
                break;
            }
        }
        return foundToDelete;
    }

    protected <E extends BaseEntity> boolean isFoundToUpdate(UUID userId, E toUpdate, E child) {
        boolean foundToUpdate = false;
        if (toUpdate.getId().equals(child.getId())) {
            foundToUpdate = true;
            toUpdate.setUpdatedBy(userId);
        }
        return foundToUpdate;
    }
}
