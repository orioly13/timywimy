package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.BaseEntity;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class EventRepositoryImpl extends AbstractEventTaskEntityRepository<Event> implements EventRepository {

//    private static final Set<String> extensions;
//    private static final Set<String> tasks;
//
//    static {
//
//    }

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
        return super.save(entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        return delete(Event.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Event event) {
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        assertDelete(event.getId());
        return delete(Event.class, event);
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

    private <E extends BaseEntity> void persistEntity(UUID userId, E toAdd) {
        toAdd.setCreatedBy(userId);
        toAdd.setId(null);
        entityManager.persist(toAdd);
    }

    private <E extends BaseEntity> boolean isFoundToDelete(E toDelete, List<E> children) {
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

    private <E extends BaseEntity> boolean isFoundToUpdate(UUID userId, E toUpdate, E child) {
        boolean foundToUpdate = false;
        if (toUpdate.getId().equals(child.getId())) {
            foundToUpdate = true;
            toUpdate.setUpdatedBy(userId);
        }
        return foundToUpdate;
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> addExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        List<AbstractEventExtension> eventExtensions = event.getExtensions();
        for (AbstractEventExtension toAdd : extensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "extension");

            toAdd.setEvent(event);
            persistEntity(userId, toAdd);
            eventExtensions.add(toAdd);
        }
        Set<String> extensionParameter = new HashSet<>();
        extensionParameter.add("extensions");
        return get(eventId, extensionParameter).getExtensions();
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> updateExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        Event event = get(eventId);
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
        Set<String> extensionParameter = new HashSet<>();
        extensionParameter.add("extensions");
        return get(eventId, extensionParameter).getExtensions();
    }

    @Override
    @Transactional
    public List<AbstractEventExtension> deleteExtensions(UUID eventId, List<AbstractEventExtension> extensions, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, extensions, "extensions");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (AbstractEventExtension toDelete : extensions) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "extension");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "extension id");

            if (!isFoundToDelete(toDelete, event.getExtensions())) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete extensions that don't exist in event");
            }
        }
        Set<String> extensionParameter = new HashSet<>();
        extensionParameter.add("extensions");
        return get(eventId, extensionParameter).getExtensions();
    }

    @Override
    public List<Task> addTasks(UUID eventId, List<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, tasks, "tasks");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");
        List<Task> eventTasks = event.getTasks();
        for (Task toAdd : tasks) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "task");

            toAdd.setEvent(event);
            persistEntity(userId, toAdd);
            eventTasks.add(toAdd);

        }
        Set<String> taskParameter = new HashSet<>();
        taskParameter.add("tasks");
        return get(eventId, taskParameter).getTasks();
    }


    @Override
    public List<Task> updateTasks(UUID eventId, List<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, tasks, "tasks");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (Task toUpdate : tasks) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate, "task");
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate.getId(), "task id");

            boolean foundToUpdate = false;
            //without transaction proxy can't be initialized
            for (Task task : event.getTasks()) {
                foundToUpdate = isFoundToUpdate(userId, toUpdate, task);
                if (foundToUpdate) {
                    toUpdate.setEvent(event);
                    entityManager.merge(toUpdate);
                    break;
                }
            }

            if (!foundToUpdate) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to update tasks that don't exist in event");
            }
        }

        Set<String> taskParameter = new HashSet<>();
        taskParameter.add("tasks");
        return get(eventId, taskParameter).getTasks();
    }

    @Override
    public List<Task> deleteTasks(UUID eventId, List<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, eventId, "event");
        RequestUtil.validateEmptyField(RepositoryException.class, tasks, "tasks");
        Event event = get(eventId);
        RequestUtil.validateEmptyField(RepositoryException.class, event, "event");

        for (Task toDelete : tasks) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "task");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "task id");

            if (!isFoundToDelete(toDelete, event.getTasks())) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete extensions that don't exist in event");
            }
        }
        Set<String> taskParameter = new HashSet<>();
        taskParameter.add("tasks");
        return get(eventId, taskParameter).getTasks();
    }
}
