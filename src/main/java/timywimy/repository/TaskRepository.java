package timywimy.repository;

import timywimy.model.bo.tasks.Task;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends EventTaskEntityRepository<Task> {

    List<Task> linkChildren(UUID taskId, List<Task> children, UUID userId);

    List<Task> unlinkChildren(UUID taskId, List<Task> children, UUID userId);
}
