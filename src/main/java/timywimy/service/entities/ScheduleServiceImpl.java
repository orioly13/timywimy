package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import timywimy.model.security.User;
import timywimy.repository.EventRepository;
import timywimy.repository.ScheduleRepository;
import timywimy.service.RestService;
import timywimy.util.Converter;
import timywimy.util.CronEntity;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
        RequestUtil.validateEmptyField(ServiceException.class, dto.getCron(), "schedule cron");
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
        if (!dto.getCron().equals(schedule.getCron())) {
            if (!CronEntity.assertCron(dto.getCron())) {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "cron is invalid format (minutes,hours,days of month,months of year,days of week * * * * *)");
            }
        }
        schedule.setOwner(userBySession);
        schedule.setName(dto.getName());
        schedule.setDescription(dto.getDescription());
        if (schedule.getId() != null && (!dto.getCron().equals(schedule.getCron()) ||
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
            RequestUtil.validateEmptyField(ServiceException.class, instance.getDateTimeZone(), "instance dateTimeZone");
        }
        //load schedule
        timywimy.model.bo.events.Schedule schedLoaded = repository.get(schedule);
        if (schedLoaded == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "schedule not found");
        }
        assertOwner(schedLoaded, userBySession);
        CronEntity cronEntity = new CronEntity(schedLoaded.getCron());

        List<timywimy.model.bo.events.Event> eventsToAdd = new ArrayList<>();
        for (Event instance : instances) {
            timywimy.model.bo.events.Event event = Converter.eventDTOToEventEntity(instance);
            //check instance on schedule
            ZonedDateTime zonedDateTime = event.getDateTimeZone().getZonedDateTime();
            if (schedLoaded.getZone() != null && event.getDateTimeZone().getZone() == null ||
                    schedLoaded.getZone() == null && event.getDateTimeZone().getZone() != null) {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "instance should have zone if schedule does (and vice versa)");
            }
            if (schedLoaded.getZone() != null) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(schedLoaded.getZone());
            }
            if (!cronEntity.validateLocalDateTime(zonedDateTime.toLocalDateTime())) {
                throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "event instance differs from cron");
            }
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

        timywimy.model.bo.events.Schedule schedLoaded = repository.get(schedule, RequestUtil.parametersSet("instances"));
        if (schedLoaded == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "schedule not found");
        }
        assertOwner(schedLoaded, userBySession);

        List<timywimy.model.bo.events.Event> loadedInstances = schedLoaded.getInstances();
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

    @Override
    public List<ZonedDateTime> getNextOccurrences(UUID schedule, UUID session, ZonedDateTime start,
                                                  Integer days, Integer maxAmount) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, schedule, "schedule");
        RequestUtil.validateEmptyField(ServiceException.class, start, "start datetime");
        if (days != null && days > CronEntity.MAX_DAYS_COUNT) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "max days is " + CronEntity.MAX_DAYS_COUNT);
        }
        if (maxAmount != null && maxAmount > CronEntity.MAX_INSTANCES_COUNT) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "max occurrences is " + CronEntity.MAX_INSTANCES_COUNT);
        }
        timywimy.model.bo.events.Schedule schedLoaded = repository.get(schedule);
        if (schedLoaded == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "schedule not found");
        }
        assertOwner(schedLoaded, userBySession);
        CronEntity entity = new CronEntity(schedLoaded.getCron());
        ZonedDateTime startAdjusted = schedLoaded.getZone() == null ? start :
                start.withZoneSameInstant(schedLoaded.getZone());
        List<LocalDateTime> localDateTimes = entity.nextLocalDateTimeList(startAdjusted.toLocalDateTime(), days, maxAmount);

        List<ZonedDateTime> res = new ArrayList<>();
        for (LocalDateTime localDateTime : localDateTimes) {
            res.add(ZonedDateTime.of(localDateTime,
                    schedLoaded.getZone() == null ? start.getZone() : schedLoaded.getZone()));
        }
        return res;
    }
}
