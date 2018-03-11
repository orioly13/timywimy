package timywimy.repository;

import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends EventTaskEntityRepository<timywimy.model.bo.events.Event> {

    List<AbstractEventExtension> addExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<AbstractEventExtension> updateExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<AbstractEventExtension> deleteExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<Task> addTasks(UUID eventId, List<Task> tasks, UUID userId);

    List<Task> updateTasks(UUID eventId, List<Task> tasks, UUID userId);

    List<Task> deleteTasks(UUID eventId, List<Task> tasks, UUID userId);
}
