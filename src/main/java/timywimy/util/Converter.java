package timywimy.util;

import org.hibernate.Hibernate;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;
import timywimy.web.dto.events.extensions.CounterExtension;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.events.extensions.TickBoxExtension;
import timywimy.web.dto.security.User;
import timywimy.web.dto.tasks.Task;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private Converter() {
    }

    public static User userEntityToUserDTO(timywimy.model.security.User entity) {
        if (entity == null)
            return null;
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail().toLowerCase());
        dto.setName(entity.getName());
        dto.setBanned(entity.isBanned());
        dto.setBannedTill(entity.getBannedTill());
        dto.setRole(entity.getRole());
        return dto;
    }

    public static timywimy.model.security.User userDTOtoUserEntity(User dto) {
        if (dto == null) {
            return null;
        }
        timywimy.model.security.User entity = new timywimy.model.security.User();
        //to lowercase to prevent changes
        entity.setEmail(dto.getEmail().toLowerCase());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        entity.setBanned(dto.isBanned() == null ? false : dto.isBanned());
        entity.setBannedTill(dto.getBannedTill());
        return entity;
    }

    public static Task taskEntityToTaskDTO(timywimy.model.bo.tasks.Task entity, boolean initLazyFields) {
        if (entity == null)
            return null;
        Task dto = new Task();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPriority(entity.getPriority());
        dto.setCompleted(entity.isCompleted());
        dto.setDeadline(dateTimeZoneEntityToDTO(entity.getDateTimeZone()));
        if (initLazyFields) {
            if (Hibernate.isInitialized(entity.getParent()) && entity.getParent() != null) {
                dto.setParent(taskEntityToTaskDTO(entity.getParent(), false));
            }
            if (Hibernate.isInitialized(entity.getChildren()) && entity.getChildren() != null) {
                List<Task> taskList = new ArrayList<>();
                for (timywimy.model.bo.tasks.Task task : entity.getChildren()) {
                    taskList.add(taskEntityToTaskDTO(task, false));
                }
                dto.setChildren(taskList);
            }
            if (Hibernate.isInitialized(entity.getEvent()) && entity.getEvent() != null) {
                dto.setEvent(eventEntityToEventDTO(entity.getEvent(), false));
            }
        }
        return dto;
    }

    public static Event eventEntityToEventDTO(timywimy.model.bo.events.Event entity, boolean initLazyFields) {
        if (entity == null)
            return null;
        Event dto = new Event();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDuration(entity.getDuration());
        dto.setDateTimeZone(dateTimeZoneEntityToDTO(entity.getDateTimeZone()));
        if (initLazyFields) {
            if (Hibernate.isInitialized(entity.getSchedule()) && entity.getSchedule() != null) {
                dto.setSchedule(scheduleEntityToScheduleDTO(entity.getSchedule(), false));
            }
            if (Hibernate.isInitialized(entity.getTasks()) && entity.getTasks() != null) {
                List<Task> taskList = new ArrayList<>();
                for (timywimy.model.bo.tasks.Task task : entity.getTasks()) {
                    taskList.add(taskEntityToTaskDTO(task, false));
                }
                dto.setTasks(taskList);
            }
            if (Hibernate.isInitialized(entity.getExtensions()) && entity.getExtensions() != null) {
                List<EventExtension> eventExtensions = new ArrayList<>();
                for (AbstractEventExtension extension : entity.getExtensions()) {
                    eventExtensions.add(extensionEntityToExtensionDTO(extension, false));
                }
                dto.setEventExtensions(eventExtensions);
            }
        }
        return dto;
    }

    public static timywimy.model.bo.events.Event eventDTOToEventEntity(Event dto) {
        if (dto == null)
            return null;
        timywimy.model.bo.events.Event entity = new timywimy.model.bo.events.Event();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDuration(dto.getDuration());
        entity.setDateTimeZone(dateTimeZoneDTOToEntity(dto.getDateTimeZone()));
        //no need for other fields, should be added separately
        return entity;
    }

    public static AbstractEventExtension extensionDTOToExtensionEntity(EventExtension dto) {
        if (dto == null)
            return null;
        AbstractEventExtension entity;
        if (dto instanceof CounterExtension) {
            timywimy.model.bo.events.extensions.CounterExtension counter =
                    new timywimy.model.bo.events.extensions.CounterExtension();
            counter.setOrder(dto.getOrder());
            counter.setName(dto.getName());
            counter.setCounter(((CounterExtension) dto).getCounter());
            entity = counter;
        } else if (dto instanceof TickBoxExtension) {
            timywimy.model.bo.events.extensions.TickBoxExtension tickBox =
                    new timywimy.model.bo.events.extensions.TickBoxExtension();
            tickBox.setOrder(dto.getOrder());
            tickBox.setName(dto.getName());
            tickBox.setActive(((TickBoxExtension) dto).getActive());
            entity = tickBox;
        } else {
            throw new ServiceException(ErrorCode.INTERNAL_SERVICE, "unable to convert dto to entity");
        }
        return entity;
    }

    public static EventExtension extensionEntityToExtensionDTO(AbstractEventExtension extension, boolean initLazyFields) {
        if (extension == null)
            return null;
        EventExtension dto;
        if (extension instanceof timywimy.model.bo.events.extensions.CounterExtension) {
            CounterExtension counter = new CounterExtension();
            counter.setOrder(extension.getOrder());
            counter.setName(extension.getName());
            counter.setCounter(((timywimy.model.bo.events.extensions.CounterExtension) extension).getCounter());
            dto = counter;
        } else if (extension instanceof timywimy.model.bo.events.extensions.TickBoxExtension) {
            TickBoxExtension tickBox = new TickBoxExtension();
            tickBox.setOrder(extension.getOrder());
            tickBox.setName(extension.getName());
            tickBox.setActive(((timywimy.model.bo.events.extensions.TickBoxExtension) extension).isActive());
            dto = tickBox;
        } else {
            throw new ServiceException(ErrorCode.INTERNAL_SERVICE, "unable to convert entity to dto");
        }
        dto.setId(extension.getId());
        return dto;
    }

    public static Schedule scheduleEntityToScheduleDTO(timywimy.model.bo.events.Schedule schedule, boolean initLazyFields) {
        if (schedule == null)
            return null;
        Schedule dto = new Schedule();
        dto.setId(schedule.getId());
        dto.setCron(schedule.getCron());
        dto.setName(schedule.getName());
        dto.setDescription(schedule.getDescription());
        dto.setDuration(schedule.getDuration());
        if (initLazyFields) {
            if (Hibernate.isInitialized(schedule.getInstances()) && schedule.getInstances() != null) {
                List<Event> events = new ArrayList<>();
                for (timywimy.model.bo.events.Event event : schedule.getInstances()) {
                    events.add(eventEntityToEventDTO(event, false));
                }
                dto.setInstances(events);
            }
        }

        return dto;
    }

    public static DateTimeZone dateTimeZoneEntityToDTO(timywimy.model.common.util.DateTimeZone dateTimeZone) {
        return dateTimeZone == null ? null : new DateTimeZone(
                dateTimeZone.getDate(), dateTimeZone.getTime(), dateTimeZone.getZone());
    }

    public static timywimy.model.common.util.DateTimeZone dateTimeZoneDTOToEntity(DateTimeZone dateTimeZone) {
        return dateTimeZone == null ? null : new timywimy.model.common.util.DateTimeZone(
                dateTimeZone.getDate(), dateTimeZone.getTime(), dateTimeZone.getZone());
    }


    public static timywimy.model.common.util.DateTimeZone zonedDateTimeToDTZ(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return new timywimy.model.common.util.DateTimeZone(zonedDateTime.toLocalDate(),
                zonedDateTime.toLocalTime(), zonedDateTime.getZone());
    }

}
