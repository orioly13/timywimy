package timywimy.web.controllers.entities;


import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.tasks.Task;

import java.util.List;
import java.util.UUID;

public interface EventController extends EntityController<Event> {

    Response<List<Event>> getBetween(Integer requestId, UUID session, String start, String finish);

    Response<List<Task>> linkTasks(Integer requestId, UUID session, UUID event, List<Task> tasks);

    Response<List<Task>> unlinkTasks(Integer requestId, UUID session, UUID event, List<Task> tasks);

    Response<Event> addExtensions(Integer requestId, UUID session, UUID event, List<EventExtension> extensions);

    Response<Event> updateExtensions(Integer requestId, UUID session, UUID event, List<EventExtension> extensions);

    Response<Event> deleteExtensions(Integer requestId, UUID session, UUID event, List<EventExtension> extensions);
}
