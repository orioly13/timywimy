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
import java.util.Iterator;
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
                orderBy(builder.asc(userRoot.get("owner")),
                        builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Task> getAllByOwner(UUID owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
        Root<Task> userRoot = criteria.from(Task.class);
        User user = new User();
        user.setId(owner);
        criteria.select(userRoot).
                where(builder.equal(userRoot.get("owner"), user)).
                orderBy(builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Task> getByOwnerBetween(UUID owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        List<Task> allByOwner = getAllByOwner(owner);
        return getBetween(allByOwner, start, end);
    }

    @Override
    @Transactional
    public List<Task> linkChildren(UUID taskId, List<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskId, "parent task");
        RequestUtil.validateEmptyField(RepositoryException.class, children, "children tasks");
        Task task = get(taskId);
        RequestUtil.validateEmptyField(RepositoryException.class, task, "parent task");
        List<Task> taskChildren = task.getChildren();
        for (Task toLink : children) {
            RequestUtil.validateEmptyField(RepositoryException.class, toLink, "task");
            RequestUtil.validateEmptyField(RepositoryException.class, toLink, "task id");
            Task toLinkLoaded = entityManager.find(Task.class, toLink.getId());
            RequestUtil.validateEmptyField(RepositoryException.class, toLinkLoaded, "task");

            toLinkLoaded.setParent(task);
            toLinkLoaded.setUpdatedBy(userId);
            entityManager.merge(toLinkLoaded);
            taskChildren.add(toLinkLoaded);

        }
        entityManager.flush();
        return get(taskId, RequestUtil.parametersSet("children")).getChildren();
    }

    @Override
    @Transactional
    public List<Task> unlinkChildren(UUID taskId, List<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, taskId, "parent task");
        RequestUtil.validateEmptyField(RepositoryException.class, children, "children tasks");
        Task task = get(taskId);
        RequestUtil.validateEmptyField(RepositoryException.class, task, "parent task");

        for (Task toUnlink : children) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUnlink, "child task");
            RequestUtil.validateEmptyField(RepositoryException.class, toUnlink.getId(), "child task id");

            boolean foundToUnlink = false;
            Iterator<Task> iterator = task.getChildren().iterator();
            while (iterator.hasNext()) {
                Task child = iterator.next();
                if (toUnlink.getId().equals(child.getId())) {
                    foundToUnlink = true;
                    child.setParent(null);
                    entityManager.merge(child);
                    iterator.remove();
                    break;
                }
            }
            if (!foundToUnlink) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to unlink tasks that don't exist in task");
            }
        }
        entityManager.flush();
        return get(taskId, RequestUtil.parametersSet("children")).getChildren();
    }
}
