package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.tasks.Task;
import timywimy.model.bo.tasks.TaskGroup;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractOwnedEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class TaskGroupRepositoryImpl extends AbstractOwnedEntityRepository<TaskGroup> implements TaskGroupRepository {

    @Override
    public TaskGroup get(UUID id) {
        assertGet(id);
        return get(TaskGroup.class, id);
    }

    @Override
    public TaskGroup get(UUID id, Set<String> properties) {
        assertGet(id);
        return get(TaskGroup.class, id, properties);
    }

    @Override
    @Transactional
    public TaskGroup save(TaskGroup entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return super.save(entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        return delete(TaskGroup.class, id);
    }

    @Override
    public boolean delete(TaskGroup entity) {
        return false;
    }

    @Override
    public List<TaskGroup> getAll() {
        CriteriaQuery<TaskGroup> criteria = builder.createQuery(TaskGroup.class);
        Root<TaskGroup> userRoot = criteria.from(TaskGroup.class);
        criteria.select(userRoot).
                orderBy(builder.asc(userRoot.get("owner.id")),
                builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<TaskGroup> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return owner.getTaskGroups();
    }

    @Override
    public Collection<Task> addTasks(UUID taskGroupId, Collection<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskGroupId, "event");
        TaskGroup schedule = get(taskGroupId);
        schedule.getTasks().addAll(tasks);
        return save(schedule, userId).getTasks();
    }

    @Override
    public Collection<Task> removeTasks(UUID taskGroupId, Collection<Task> tasks, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskGroupId, "event");
        TaskGroup task = get(taskGroupId);
        Collection<Task> result = new ArrayList<>();
        for (Task event : task.getTasks()) {
            boolean foundToRemove = false;
            for (Task toRemove : tasks) {
                if (event.getId().equals(toRemove.getId())) {
                    foundToRemove = true;
                    break;
                }
            }
            if (!foundToRemove) {
                result.add(event);
            }
        }
        task.setTasks(result);
        return save(task, userId).getTasks();
    }

    @Override
    public Collection<TaskGroup> addSubGroups(UUID taskGroupId, Collection<TaskGroup> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskGroupId, "event");
        TaskGroup schedule = get(taskGroupId);
        schedule.getChildren().addAll(children);
        return save(schedule, userId).getChildren();
    }

    @Override
    public Collection<TaskGroup> removeSubGroups(UUID taskGroupId, Collection<TaskGroup> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskGroupId, "event");
        TaskGroup task = get(taskGroupId);
        Collection<TaskGroup> result = new ArrayList<>();
        for (TaskGroup event : task.getChildren()) {
            boolean foundToRemove = false;
            for (TaskGroup toRemove : children) {
                if (event.getId().equals(toRemove.getId())) {
                    foundToRemove = true;
                    break;
                }
            }
            if (!foundToRemove) {
                result.add(event);
            }
        }
        task.setChildren(result);
        return save(task, userId).getChildren();
    }
}
