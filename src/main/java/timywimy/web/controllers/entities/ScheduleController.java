package timywimy.web.controllers.entities;


import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;
import timywimy.web.dto.tasks.Task;

import java.util.List;
import java.util.UUID;

public interface ScheduleController extends EntityController<Schedule> {


    Response<List<Event>> addInstances(Integer requestId, UUID session, UUID schedule, List<Event> instances);

    Response<List<Event>> deleteInstances(Integer requestId, UUID session, UUID schedule, List<Event> instances);
}
