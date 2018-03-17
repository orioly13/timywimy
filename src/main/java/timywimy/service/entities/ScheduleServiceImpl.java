package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.repository.EventRepository;
import timywimy.repository.ScheduleRepository;
import timywimy.service.RestService;
import timywimy.service.converters.Converter;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;

import java.util.*;

@Service
public class ScheduleServiceImpl extends AbstractOwnedEntityService<Schedule, timywimy.model.bo.events.Schedule>
        implements ScheduleService {

    private final EventRepository eventRepository;

    @Autowired
    protected ScheduleServiceImpl(RestService restService, ScheduleRepository repository, EventRepository eventRepository) {
        super(restService, repository);
        Assert.notNull(eventRepository, "Event repo should be provided");
        this.eventRepository = eventRepository;
    }

    @Override
    public Schedule get(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, entityId, "entityId");


        timywimy.model.bo.events.Schedule schedule = repository.get(entityId);
        if (schedule == null) {
            return null;
        }
        assertOwner(schedule, userBySession);

        return Converter.scheduleEntityToScheduleDTO(schedule);
    }

    @Override
    public Schedule save(Schedule dto, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, dto, "task");
        //todo check if event has schedule (should be consistent with schedule cron)
        timywimy.model.bo.events.Schedule schedule = repository.get(dto.getId());
        if (schedule == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "schedule not found");
        }
        assertOwner(schedule, userBySession);

        schedule.setName(dto.getName());
        schedule.setDescription(dto.getDescription());
        schedule.setDuration(dto.getDuration());
        schedule.setCron(dto.getCron());


        return Converter.scheduleEntityToScheduleDTO(repository.save(schedule, userSession));
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, entityId, "entityId");

        timywimy.model.bo.events.Schedule schedule = repository.get(entityId);
        if (schedule == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "task not found");
        }
        assertOwner(schedule, userBySession);

        return repository.delete(schedule);
    }

    @Override
    public List<Schedule> getAll(UUID userSession) {
        User userBySession = getUserBySession(userSession);

        List<timywimy.model.bo.events.Schedule> allByOwner = ((ScheduleRepository) repository).
                getAllByOwner(userBySession.getId());

        List<Schedule> result = new ArrayList<>();
        for (timywimy.model.bo.events.Schedule ownedSchedule : allByOwner) {
            result.add(Converter.scheduleEntityToScheduleDTO(ownedSchedule));
        }
        return result;
    }


    @Override
    public List<Event> addInstances(UUID schedule, UUID session, List<Event> instances) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, schedule, "schedule");
        for (Event instance : instances) {
            RequestUtil.validateEmptyField(ServiceException.class, instance, "instance");
            if (instance.getId() != null) {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "instance id exists");
            }
        }

        List<timywimy.model.bo.events.Event> eventsToAdd = new ArrayList<>();
        for (Event instance : instances) {
            eventsToAdd.add(Converter.eventDTOToEventEntity(instance));
        }

        List<timywimy.model.bo.events.Event> events = ((ScheduleRepository) repository).
                addInstances(schedule, eventsToAdd, userBySession.getId());

        List<Event> result = new ArrayList<>();
        for (timywimy.model.bo.events.Event ownedEvent : events) {
            result.add(Converter.eventEntityToEventDTO(ownedEvent));
        }

        return result;
    }

    @Override
    public List<Event> deleteInstances(UUID schedule, UUID session, List<Event> instances) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, schedule, "schedule");
        for (Event instance : instances) {
            RequestUtil.validateEmptyField(ServiceException.class, instance, "instance");
            RequestUtil.validateEmptyField(ServiceException.class, instance.getId(), "instance id");

        }

        Set<String> parameters = new HashSet<>();
        parameters.add("instances");
        List<timywimy.model.bo.events.Event> loadedInstances = repository.get(schedule, parameters).getInstances();
        List<timywimy.model.bo.events.Event> instancesToRemove = new ArrayList<>();
        for (Event instance : instances) {
            boolean foundToDelete = false;
            for (timywimy.model.bo.events.Event loadedInstance : loadedInstances) {
                if (loadedInstance.getId().equals(instance.getId())) {
                    foundToDelete = true;
                    instancesToRemove.add(loadedInstance);
                    break;
                }
            }
            if (!foundToDelete) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of instances not found");
            }
        }

        List<timywimy.model.bo.events.Event> resultInstances = ((ScheduleRepository) repository).
                deleteInstances(schedule, instancesToRemove, userBySession.getId());


        List<Event> result = new ArrayList<>();
        for (timywimy.model.bo.events.Event resultInstance : resultInstances) {
            result.add(Converter.eventEntityToEventDTO(resultInstance));
        }
        return result;
    }
}
