package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class TaskRepositoryImpl extends AbstractEventTaskEntityRepository<Task> implements TaskRepository {

    @Override
    public Task get(UUID id) {
        assertGet(id);
        return getBaseEntity(Task.class, id);
    }

    @Override
    public Task get(UUID id, Set<String> properties) {
        assertGet(id);
        return getBaseEntity(Task.class, id, properties);
    }

    @Override
    @Transactional
    public Task save(Task entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getDescription(), "description");
        return saveBaseEntity(entity, userId);
    }

    private void unlinkTasks(Task parent) {
        List<Task> children = parent.getChildren();
        if (children.size() > 0) {
            for (Task task : children) {
                task.setParent(null);
                entityManager.merge(task);
            }
            entityManager.flush();
        }
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        unlinkTasks(get(id));
        return deleteBaseEntity(Task.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Task task) {
        RequestUtil.validateEmptyField(RepositoryException.class, task, "task");
        assertDelete(task.getId());
        unlinkTasks(get(task.getId()));
        return deleteBaseEntity(Task.class, task);
    }

    @Override
    public List<Task> getAll() {
        CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
        Root<Task> userRoot = criteria.from(Task.class);
        criteria.select(userRoot).
                orderBy(builder.asc(userRoot.get("owner.id")),
                        builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Task> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return owner.getTasks();
    }

    @Override
    public List<Task> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        List<Task> allByOwner = getAllByOwner(owner);
        return getBetween(allByOwner, start, end);
    }

    @Override
    @Transactional
    public List<Task> addTasks(UUID taskId, List<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskId, "parent task");
        RequestUtil.validateEmptyField(RepositoryException.class, children, "children tasks");
        Task task = get(taskId);
        RequestUtil.validateEmptyField(RepositoryException.class, task, "parent task");
        List<Task> taskChildren = task.getChildren();
        for (Task toAdd : children) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "task");

            toAdd.setParent(task);
            persistEntity(userId, toAdd);
            taskChildren.add(toAdd);

        }

        return get(taskId, constructParametersSet("children")).getChildren();
    }

    @Override
    @Transactional
    public List<Task> updateTasks(UUID taskId, List<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskId, "parent task");
        RequestUtil.validateEmptyField(RepositoryException.class, children, "children tasks");
        Task task = get(taskId);
        RequestUtil.validateEmptyField(RepositoryException.class, task, "parent task");

        for (Task toUpdate : children) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate, "child task");
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate.getId(), "child task id");

            boolean foundToUpdate = false;
            //without transaction proxy can't be initialized
            for (Task child : task.getChildren()) {
                foundToUpdate = isFoundToUpdate(userId, toUpdate, child);
                if (foundToUpdate) {
                    toUpdate.setParent(child);
                    entityManager.merge(toUpdate);
                    break;
                }
            }

            if (!foundToUpdate) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to update tasks that don't exist in parent task");
            }
        }

        return get(taskId, constructParametersSet("children")).getChildren();
    }

    @Override
    @Transactional
    public List<Task> deleteTasks(UUID taskId, List<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskId, "parent task");
        RequestUtil.validateEmptyField(RepositoryException.class, children, "children tasks");
        Task task = get(taskId);
        RequestUtil.validateEmptyField(RepositoryException.class, task, "parent task");

        for (Task toDelete : children) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "child task");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "child task id");

            if (!isFoundToDelete(toDelete, task.getChildren())) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete tasks that don't exist in parent task");
            }
        }

        return get(taskId, constructParametersSet("children")).getChildren();
    }
}
