package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.common.DateTimeZone;
import timywimy.repository.common.AbstractDateTimeZoneEntityRepository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class EventRepositoryImpl extends AbstractDateTimeZoneEntityRepository<Event> implements EventRepository {

    @Override
    public Event get(UUID id) {
        return get(Event.class, id);
    }

    @Override
    @Transactional
    public Event save(Event entity, UUID userId) {
        return save(Event.class, entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID userId) {
        return delete(Event.class, id, userId);
    }

    @Override
    public List<Event> getAll() {

        CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
        Root<Event> userRoot = criteria.from(Event.class);
        criteria.select(userRoot).where(getDeletedTsExpression(userRoot, null))
                .orderBy(builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Event> getBetweenOwned(DateTimeZone start, DateTimeZone end, UUID ownerId) {
        return getBetweenOwned(Event.class, start, end, ownerId);
    }
}
