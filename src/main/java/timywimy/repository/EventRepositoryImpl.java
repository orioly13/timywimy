package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.common.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class EventRepositoryImpl extends AbstractEventTaskEntityRepository<Event> implements EventRepository {

    @Override
    public Event get(UUID id) {
        assertGet(id);
        return get(Event.class, id);
    }

    @Override
    @Transactional
    public Event save(Event entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return save(Event.class, entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID userId) {
        assertDelete(id, userId);
        return delete(Event.class, id, userId);
    }

    @Override
    public List<Event> getAll() {
        CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
        Root<Event> userRoot = criteria.from(Event.class);
        criteria.select(userRoot).
                orderBy(builder.asc(userRoot.get("owner.id")),
                        builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Collection<Event> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return owner.getEvents();
    }

    @Override
    public Collection<Event> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        Collection<Event> allByOwner = getAllByOwner(owner);
        return getBetween(allByOwner, start, end);
    }

    @Override
    @Transactional
    public Collection<AbstractEventExtension> addEventExtensions(UUID eventId, Collection<AbstractEventExtension> eventExtensions, UUID userId) {
        //todo check if NOT connected before adding
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        Event event = get(eventId);
        event.getExtensions().addAll(eventExtensions);
        return save(event, userId).getExtensions();
    }

    @Override
    @Transactional
    public Collection<AbstractEventExtension> removeEventExtension(UUID eventId, Collection<AbstractEventExtension> eventExtensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        Event event = get(eventId);
        Collection<AbstractEventExtension> result = new ArrayList<>();
        for (AbstractEventExtension extension : event.getExtensions()) {
            boolean foundToRemove = false;
            for (AbstractEventExtension toRemove : eventExtensions) {
                if (extension.getId().equals(toRemove.getId())) {
                    foundToRemove = true;
                    break;
                }
            }
            if (!foundToRemove) {
                result.add(extension);
            }
        }
        event.setExtensions(result);
        return save(event, userId).getExtensions();
    }
}
