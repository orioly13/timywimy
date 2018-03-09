package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
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
public class EventRepositoryImpl extends AbstractEventTaskEntityRepository<Event> implements EventRepository {

    @Override
    public Event get(UUID id) {
        assertGet(id);
        return get(Event.class, id);
    }

    @Override
    public Event get(UUID id, Set<String> properties) {
        assertGet(id);
        return get(Event.class, id, properties);
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
    public List<Event> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return owner.getEvents();
    }

    @Override
    public List<Event> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        List<Event> allByOwner = getAllByOwner(owner);
        return getBetween(allByOwner, start, end);
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> addExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, eventExtensions, "extensions");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        for (AbstractEventExtension toAdd : eventExtensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "extension");

            toAdd.setCreatedBy(userId);
            toAdd.setEvent(event);
            toAdd.setId(null);
            entityManager.persist(toAdd);
        }
        return event.getExtensions();
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> updateExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, eventExtensions, "extensions");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (AbstractEventExtension toUpdate : eventExtensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate, "extension");
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate.getId(), "extension id");

            boolean foundToUpdate = false;
            //without transaction proxy can't be initialized
            for (AbstractEventExtension extension : event.getExtensions()) {
                if (toUpdate.getId().equals(extension.getId())) {
                    foundToUpdate = true;
                    toUpdate.setUpdatedBy(userId);
                    toUpdate.setEvent(event);
                    entityManager.merge(toUpdate);
                    break;
                }
            }

            if (!foundToUpdate) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to update extensions that don't exist in event");
            }
        }
//        entityManager.flush(); //(flush with transactional(readonly) does not work)
        return event.getExtensions();
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> deleteExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, eventExtensions, "extensions");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (AbstractEventExtension toDelete : eventExtensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "extension");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "extension id");

            boolean foundToDelete = false;
            for (AbstractEventExtension extension : event.getExtensions()) {
                if (toDelete.getId().equals(extension.getId())) {
                    foundToDelete = true;
                    entityManager.remove(extension);
                    break;
                }
            }

            if (!foundToDelete) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete extensions that don't exist in event");
            }
        }
        return event.getExtensions();
    }
}
