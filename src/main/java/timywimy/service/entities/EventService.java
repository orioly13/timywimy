package timywimy.service.entities;


import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.tasks.Task;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface EventService extends EntityService<Event, timywimy.model.bo.events.Event> {

    List<Event> getBetween(UUID session, ZonedDateTime start, ZonedDateTime finish);

    List<Task> linkTasks(UUID event, UUID session, List<Task> tasks);

    List<Task> unlinkTasks(UUID event, UUID session, List<Task> tasks);

    Event addExtensions(UUID event, UUID session, List<EventExtension> extensions);

    Event updateExtensions(UUID event, UUID session, List<EventExtension> extensions);

    Event deleteExtensions(UUID event, UUID session, List<EventExtension> extensions);

}
