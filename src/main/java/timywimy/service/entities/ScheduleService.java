package timywimy.service.entities;


import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;

import java.util.List;
import java.util.UUID;

public interface ScheduleService extends EntityService<Schedule, timywimy.model.bo.events.Schedule> {

    List<Event> addInstances(UUID schedule, UUID session, List<Event> instances);

    List<Event> deleteInstances(UUID schedule, UUID session, List<Event> instances);

}
