package timywimy.repository;

import timywimy.model.bo.tasks.Task;
import timywimy.model.bo.tasks.TaskGroup;
import timywimy.repository.common.EventTaskEntityRepository;
import timywimy.repository.common.OwnedEntityRepository;

import java.util.Collection;
import java.util.UUID;

public interface TaskGroupRepository extends OwnedEntityRepository<TaskGroup> {

    Collection<Task> addTasks(UUID taskGroupId, Collection<Task> tasks, UUID userId);

    Collection<Task> removeTasks(UUID taskGroupId, Collection<Task> tasks, UUID userId);

    Collection<TaskGroup> addSubGroups(UUID taskGroupId, Collection<TaskGroup> children, UUID userId);

    Collection<TaskGroup> removeSubGroups(UUID taskGroupId, Collection<TaskGroup> children, UUID userId);
}
