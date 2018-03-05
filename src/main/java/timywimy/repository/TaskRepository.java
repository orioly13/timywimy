package timywimy.repository;

import timywimy.model.bo.tasks.Task;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.Collection;
import java.util.UUID;

public interface TaskRepository extends EventTaskEntityRepository<Task> {

    Collection<Task> addSubtasks(UUID scheduleId, Collection<Task> children, UUID userId);

    Collection<Task> removeSubtasks(UUID scheduleId, Collection<Task> children, UUID userId);
}
