package timywimy.repository.common;

import org.springframework.util.Assert;
import timywimy.model.common.DateTimeZone;
import timywimy.model.common.DateTimeZoneEntity;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDateTimeZoneEntityRepository<T extends DateTimeZoneEntity>
        extends AbstractEntityRepository<T>
        implements DateTimeZoneEntityRepository<T> {

    protected List<T> getBetweenOwned(Class<T> entityClass, DateTimeZone start, DateTimeZone end, UUID ownerId) {
        Assert.notNull(entityClass, "entity class should be provided to construct get query");
        Assert.notNull(ownerId, "owner id should be provided");
        Assert.notNull(start, "start should be provided");
        Assert.notNull(end, "end should be provided");
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start datetime is after end datetime");
        }

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        Root<T> queryRoot = criteria.from(entityClass);
        criteria.select(queryRoot).
                where(getDeletedTsExpression(queryRoot, builder.and(
                        builder.between(queryRoot.get("dateTimeZone"), start, end),
                        builder.equal(queryRoot.get("owner.id"), ownerId)))).
                orderBy(builder.asc(queryRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }
}
