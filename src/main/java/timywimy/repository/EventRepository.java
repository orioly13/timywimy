package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends EventTaskEntityRepository<timywimy.model.bo.events.Event> {

    Event addExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    Event updateExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    Event deleteExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<Task> linkTasks(UUID eventId, List<Task> tasks, UUID userId);

    List<Task> unlinkTasks(UUID eventId, List<Task> tasks, UUID userId);
}
