package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class EventRepositoryImpl extends AbstractEventTaskEntityRepository<Event> implements EventRepository {

    @Override
    public Event get(UUID id) {
        assertGet(id);
        return getBaseEntity(timywimy.model.bo.events.Event.class, id);
    }

    @Override
    public Event get(UUID id, Set<String> properties) {
        assertGet(id);
        return getBaseEntity(timywimy.model.bo.events.Event.class, id, properties);
    }

    @Override
    @Transactional
    public Event save(timywimy.model.bo.events.Event entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return saveBaseEntity(entity, userId);
    }

    private void unlinkTasks(timywimy.model.bo.events.Event event) {
        List<Task> tasks = event.getTasks();
        if (tasks.size() > 0) {
            for (Task task : tasks) {
                task.setEvent(null);
                entityManager.merge(task);
            }
            entityManager.flush();
        }
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        unlinkTasks(get(id, RequestUtil.parametersSet("tasks")));
        return deleteBaseEntity(timywimy.model.bo.events.Event.class, id);
    }


    @Override
    @Transactional
    public boolean delete(Event event) {
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        assertDelete(event.getId());
        unlinkTasks(get(event.getId(), RequestUtil.parametersSet("tasks")));
        return deleteBaseEntity(timywimy.model.bo.events.Event.class, event);
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
    public List<Event> getAllByOwner(UUID owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");

        User ownerEntity = new User();
        ownerEntity.setId(owner);

        CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
        Root<Event> userRoot = criteria.from(Event.class);
        criteria.select(userRoot).
                where(builder.equal(userRoot.get("owner"), ownerEntity))
                .orderBy(builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Event> getByOwnerBetween(UUID owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        List<Event> allByOwner = getAllByOwner(owner);
        return getBetween(allByOwner, start, end);
    }

    @Override
    @Transactional
    public Event addExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        timywimy.model.bo.events.Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        List<AbstractEventExtension> eventExtensions = event.getExtensions();
        for (AbstractEventExtension toAdd : extensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "extension");

            toAdd.setEvent(event);
            persistEntity(userId, toAdd);
            eventExtensions.add(toAdd);
        }

        return get(eventId, RequestUtil.parametersSet("extensions"));
    }

    @Override
    @Transactional
    public Event updateExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        timywimy.model.bo.events.Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (AbstractEventExtension toUpdate : extensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate, "extension");
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate.getId(), "extension id");

            boolean foundToUpdate = false;
            //without transaction proxy can't be initialized
            for (AbstractEventExtension extension : event.getExtensions()) {
                foundToUpdate = isFoundToUpdate(userId, toUpdate, extension);
                if (foundToUpdate) {
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

        return get(eventId, RequestUtil.parametersSet("extensions"));
    }

    @Override
    @Transactional
    public Event deleteExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        timywimy.model.bo.events.Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (AbstractEventExtension toDelete : extensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "extension");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "extension id");

            if (!isFoundToDelete(toDelete, event.getExtensions())) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete extensions that don't exist in event");
            }
        }

        return get(eventId, RequestUtil.parametersSet("extensions"));
    }

    @Override
    @Transactional
    public List<Task> linkTasks(UUID eventId, List<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, tasks, "tasks");
        timywimy.model.bo.events.Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        List<Task> eventTasks = event.getTasks();
        for (Task toLink : tasks) {
            RequestUtil.validateEmptyField(RepositoryException.class, toLink, "task");
            RequestUtil.validateEmptyField(RepositoryException.class, toLink, "task id");
            Task toLinkLoaded = entityManager.find(Task.class, toLink.getId());
            RequestUtil.validateEmptyField(RepositoryException.class, toLinkLoaded, "task");

            toLinkLoaded.setEvent(event);
            toLinkLoaded.setUpdatedBy(userId);
            entityManager.merge(toLinkLoaded);
            eventTasks.add(toLinkLoaded);

        }
        entityManager.flush();
        return get(eventId, RequestUtil.parametersSet("tasks")).getTasks();
    }

    @Override
    @Transactional
    public List<Task> unlinkTasks(UUID eventId, List<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, tasks, "tasks");
        timywimy.model.bo.events.Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        for (Task toUnlink : tasks) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUnlink, "task");
            RequestUtil.validateEmptyField(RepositoryException.class, toUnlink.getId(), "task id");

            boolean foundToUnlink = false;
            Iterator<Task> iterator = event.getTasks().iterator();
            while (iterator.hasNext()) {
                Task eventTask = iterator.next();
                if (toUnlink.getId().equals(eventTask.getId())) {
                    foundToUnlink = true;
                    eventTask.setEvent(null);
                    entityManager.merge(eventTask);
                    iterator.remove();
                    break;
                }
            }
            if (!foundToUnlink) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to unlink tasks that don't exist in event");
            }
        }
        entityManager.flush();
        return get(eventId, RequestUtil.parametersSet("tasks")).getTasks();
    }
}
