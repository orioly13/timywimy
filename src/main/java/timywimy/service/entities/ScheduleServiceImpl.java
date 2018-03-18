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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        timywimy.model.bo.events.Schedule schedule = repository.get(entityId,
                RequestUtil.parametersSet("owner", "instances"));
        if (schedule == null) {
            return null;
        }
        assertOwner(schedule, userBySession);

        return Converter.scheduleEntityToScheduleDTO(schedule, true);
    }

    @Override
    public Schedule save(Schedule dto, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, dto, "schedule");
        //todo assert cron if provided ()
        timywimy.model.bo.events.Schedule schedule = dto.getId() == null ? null :
                repository.get(dto.getId(), RequestUtil.parametersSet("owner", "instances"));
        if (dto.getId() != null && schedule == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "schedule not found");
        }
        if (schedule != null) {
            assertOwner(schedule, userBySession);
        } else {
            schedule = new timywimy.model.bo.events.Schedule();
        }
        schedule.setOwner(userBySession);
        schedule.setName(dto.getName());
        schedule.setDescription(dto.getDescription());
        if (schedule.getId() != null &&
                (!(dto.getCron() == null ? schedule.getCron() == null : dto.getCron().equals(schedule.getCron())) ||
                        !(dto.getDuration() == null ? schedule.getDuration() == null : dto.getDuration().equals(schedule.getDuration())))) {
            for (timywimy.model.bo.events.Event instance : schedule.getInstances()) {
                eventRepository.delete(instance);
            }
            schedule.setInstances(new ArrayList<>());
        }
        schedule.setDuration(dto.getDuration());
        schedule.setCron(dto.getCron());


        return Converter.scheduleEntityToScheduleDTO(repository.save(schedule, userBySession.getId()), true);
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);

        timywimy.model.bo.events.Schedule schedule = assertOwner(entityId, userBySession);

        return repository.delete(schedule);
    }

    @Override
    public List<Schedule> getAll(UUID userSession) {
        User userBySession = getUserBySession(userSession);

        List<timywimy.model.bo.events.Schedule> allByOwner = ((ScheduleRepository) repository).
                getAllByOwner(userBySession.getId());

        List<Schedule> result = new ArrayList<>();
        for (timywimy.model.bo.events.Schedule ownedSchedule : allByOwner) {
            result.add(Converter.scheduleEntityToScheduleDTO(ownedSchedule, true));
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
        //todo check cron of schedule
        List<timywimy.model.bo.events.Event> eventsToAdd = new ArrayList<>();
        for (Event instance : instances) {
            timywimy.model.bo.events.Event event = Converter.eventDTOToEventEntity(instance);
            event.setOwner(userBySession);
            eventsToAdd.add(event);
        }

        List<timywimy.model.bo.events.Event> events = ((ScheduleRepository) repository).
                addInstances(schedule, eventsToAdd, userBySession.getId());

        List<Event> result = new ArrayList<>();
        for (timywimy.model.bo.events.Event ownedEvent : events) {
            result.add(Converter.eventEntityToEventDTO(ownedEvent, true));
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

        List<timywimy.model.bo.events.Event> loadedInstances = repository.
                get(schedule, RequestUtil.parametersSet("instances")).getInstances();
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
            result.add(Converter.eventEntityToEventDTO(resultInstance, true));
        }
        return result;
    }
}
