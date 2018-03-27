package timywimy.web.controllers.entities;


import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleController extends EntityController<Schedule> {


    Response<List<Event>> addInstances(Integer requestId, UUID session, UUID schedule, List<Event> instances);

    Response<List<Event>> deleteInstances(Integer requestId, UUID session, UUID schedule, List<Event> instances);

    Response<List<ZonedDateTime>> getNextOccurrences(Integer requestId, UUID session, UUID schedule, String start, Integer days, Integer max);
}
