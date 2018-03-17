package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.bo.events.extensions.CounterExtension;
import timywimy.model.bo.events.extensions.TickBoxExtension;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.security.User;
import timywimy.repository.EventRepository;
import timywimy.repository.TaskRepository;
import timywimy.service.RestService;
import timywimy.service.converters.Converter;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.tasks.Task;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl extends AbstractOwnedEntityService<Event, timywimy.model.bo.events.Event>
        implements EventService {

    private final TaskRepository taskRepository;

    @Autowired
    protected EventServiceImpl(RestService restService, EventRepository repository, TaskRepository taskRepository) {
        super(restService, repository);
        Assert.notNull(taskRepository, "Task repo should be provided");
        this.taskRepository = taskRepository;
    }

    @Override
    public Event get(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, entityId, "entityId");

        timywimy.model.bo.events.Event event = repository.get(entityId, RequestUtil.parametersSet("owner"));
        if (event == null) {
            return null;
        }
        assertOwner(event, userBySession);

        return Converter.eventEntityToEventDTO(event);
    }

    private timywimy.model.bo.events.Event assertEventOwner(UUID eventId, User user) {
        RequestUtil.validateEmptyField(ServiceException.class, eventId, "event");
        timywimy.model.bo.events.Event event = repository.get(eventId, RequestUtil.parametersSet("owner"));
        if (event == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "event not found");
        }
        assertOwner(event, user);
        return event;
    }

    @Override
    public Event save(Event dto, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, dto, "event");
        timywimy.model.bo.events.Event event = dto.getId() == null ? null : repository.get(dto.getId(),
                RequestUtil.parametersSet("schedule", "owner"));
        if (dto.getId() != null && event == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "event not found");
        }
        if (event != null) {
            assertOwner(event, userBySession);
        }

        event = new timywimy.model.bo.events.Event();
        if (event.getSchedule() != null) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                    "event fields can't be updated if it's an instance of schedule");
        }
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setDuration(dto.getDuration());
        event.setDateTimeZone(Converter.dateTimeZoneDTOToEntity(dto.getDateTimeZone()));
        event.setOwner(userBySession);

        return Converter.eventEntityToEventDTO(repository.save(event, userBySession.getId()));
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);

        timywimy.model.bo.events.Event event = assertEventOwner(entityId, userBySession);

        return repository.delete(event);
    }

    @Override
    public List<Event> getAll(UUID userSession) {
        User userBySession = getUserBySession(userSession);

        List<timywimy.model.bo.events.Event> allByOwner = ((EventRepository) repository).
                getAllByOwner(userBySession.getId());

        List<Event> result = new ArrayList<>();
        for (timywimy.model.bo.events.Event ownedEvent : allByOwner) {
            result.add(Converter.eventEntityToEventDTO(ownedEvent));
        }
        return result;
    }

    @Override
    public List<Event> getBetween(UUID session, ZonedDateTime start, ZonedDateTime finish) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, start, "start");
        RequestUtil.validateEmptyField(ServiceException.class, finish, "finish");
        timywimy.model.common.util.DateTimeZone entityStart = Converter.zonedDateTimeToDTZ(start);
        timywimy.model.common.util.DateTimeZone entityFinish = Converter.zonedDateTimeToDTZ(finish);
        if (entityStart.isAfter(entityFinish)) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "start is after end");
        }

        List<timywimy.model.bo.events.Event> byOwnerBetween = ((EventRepository) repository).
                getByOwnerBetween(userBySession.getId(), entityStart, entityFinish);

        List<Event> res = new ArrayList<>();
        for (timywimy.model.bo.events.Event event : byOwnerBetween) {
            res.add(Converter.eventEntityToEventDTO(event));
        }
        return res;
    }

    @Override
    public List<Task> linkTasks(UUID event, UUID session, List<Task> tasks) {
        User userBySession = getUserBySession(session);
        assertEventOwner(event, userBySession);
        for (Task child : tasks) {
            RequestUtil.validateEmptyField(ServiceException.class, child, "event task");
            RequestUtil.validateEmptyField(ServiceException.class, child.getId(), "event task id");
        }

        List<timywimy.model.bo.tasks.Task> tasksToLink = new ArrayList<>();
        for (Task child : tasks) {
            timywimy.model.bo.tasks.Task loadedChild = taskRepository.get(child.getId());
            if (loadedChild == null) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of tasks not found");
            }
            assertOwner(loadedChild, userBySession);
            tasksToLink.add(loadedChild);
        }

        List<timywimy.model.bo.tasks.Task> eventTasks = ((EventRepository) repository).
                linkTasks(event, tasksToLink, userBySession.getId());

        List<Task> res = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task task : eventTasks) {
            res.add(Converter.taskEntityToTaskDTO(task));
        }

        return res;
    }

    @Override
    public List<Task> unlinkTasks(UUID event, UUID session, List<Task> tasks) {
        User userBySession = getUserBySession(session);
        assertEventOwner(event, userBySession);
        for (Task task : tasks) {
            RequestUtil.validateEmptyField(ServiceException.class, task, "event task");
            RequestUtil.validateEmptyField(ServiceException.class, task.getId(), "event task id");
        }

        List<timywimy.model.bo.tasks.Task> eventTasks = repository.get(event,
                RequestUtil.parametersSet("tasks")).getTasks();
        List<timywimy.model.bo.tasks.Task> tasksToUnlink = new ArrayList<>();
        for (Task task : tasks) {
            boolean foundToUnlink = false;
            for (timywimy.model.bo.tasks.Task eventTask : eventTasks) {
                if (eventTask.getId().equals(task.getId())) {
                    foundToUnlink = true;
                    tasksToUnlink.add(eventTask);
                    break;
                }
            }
            if (!foundToUnlink) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of tasks not found");
            }
        }

        List<timywimy.model.bo.tasks.Task> resultTasks = ((EventRepository) repository).
                unlinkTasks(event, tasksToUnlink, userBySession.getId());

        List<Task> res = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task task : resultTasks) {
            res.add(Converter.taskEntityToTaskDTO(task));
        }

        return res;
    }

    @Override
    public Event addExtensions(UUID event, UUID session, List<EventExtension> extensions) {
        User userBySession = getUserBySession(session);
        assertEventOwner(event, userBySession);
        for (EventExtension extension : extensions) {
            RequestUtil.validateEmptyField(ServiceException.class, extension, "extension");
            if (extension.getId() != null) {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "extension id exists");
            }
        }

        List<AbstractEventExtension> extensionsToAdd = new ArrayList<>();
        for (EventExtension extension : extensions) {
            extensionsToAdd.add(Converter.extensionDTOToExtensionEntity(extension));
        }

        timywimy.model.bo.events.Event eventWithExtensions = ((EventRepository) repository).
                addExtensions(event, extensionsToAdd, userBySession.getId());


        return Converter.eventEntityToEventDTO(eventWithExtensions);
    }

    @Override
    public Event updateExtensions(UUID event, UUID session, List<EventExtension> extensions) {
        User userBySession = getUserBySession(session);
        assertEventOwner(event, userBySession);
        for (EventExtension extension : extensions) {
            RequestUtil.validateEmptyField(ServiceException.class, extension, "extension");
            RequestUtil.validateEmptyField(ServiceException.class, extension.getId(), "extension id");

        }

        List<AbstractEventExtension> eventExtensions = repository.get(event,
                RequestUtil.parametersSet("extensions")).getExtensions();
        List<AbstractEventExtension> extensionsToUpdate = new ArrayList<>();
        for (EventExtension extension : extensions) {
            boolean foundToUpdate = false;
            for (AbstractEventExtension eventExtension : eventExtensions) {
                if (eventExtension.getId().equals(extension.getId())) {
                    foundToUpdate = true;
                    if (eventExtension instanceof CounterExtension) {
                        if (!(extension instanceof timywimy.web.dto.events.extensions.CounterExtension)) {
                            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                                    "extension class change not allowed");
                        }
                        ((CounterExtension) eventExtension).
                                setCounter(((timywimy.web.dto.events.extensions.CounterExtension) extension).getCounter());
                    } else if (eventExtension instanceof TickBoxExtension) {
                        if (!(extension instanceof timywimy.web.dto.events.extensions.TickBoxExtension)) {
                            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                                    "extension class change not allowed");
                        }
                        ((TickBoxExtension) eventExtension).
                                setActive(((timywimy.web.dto.events.extensions.TickBoxExtension) extension).getActive());
                    }
                    eventExtension.setName(extension.getName());
                    eventExtension.setOrder(extension.getOrder());
                    eventExtension.setUpdatedBy(userBySession.getId());
                    extensionsToUpdate.add(eventExtension);
                    break;
                }
            }
            if (!foundToUpdate) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of extensions not found");
            }
        }

        timywimy.model.bo.events.Event resultEvent = ((EventRepository) repository).
                updateExtensions(event, extensionsToUpdate, userBySession.getId());

        return Converter.eventEntityToEventDTO(resultEvent);
    }

    @Override
    public Event removeExtensions(UUID event, UUID session, List<EventExtension> extensions) {
        User userBySession = getUserBySession(session);
        assertEventOwner(event, userBySession);
        for (EventExtension extension : extensions) {
            RequestUtil.validateEmptyField(ServiceException.class, extension, "extension");
            RequestUtil.validateEmptyField(ServiceException.class, extension.getId(), "extension id");
        }

        List<AbstractEventExtension> eventExtensions = repository.get(event,
                RequestUtil.parametersSet("extensions")).getExtensions();
        List<AbstractEventExtension> extensionsToRemove = new ArrayList<>();
        for (EventExtension extension : extensions) {
            boolean foundToUnlink = false;
            for (AbstractEventExtension eventExtension : eventExtensions) {
                if (eventExtension.getId().equals(extension.getId())) {
                    foundToUnlink = true;
                    extensionsToRemove.add(eventExtension);
                    break;
                }
            }
            if (!foundToUnlink) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of extensions not found");
            }
        }

        timywimy.model.bo.events.Event resultEvent = ((EventRepository) repository).
                deleteExtensions(event, extensionsToRemove, userBySession.getId());

        return Converter.eventEntityToEventDTO(resultEvent);
    }
}
