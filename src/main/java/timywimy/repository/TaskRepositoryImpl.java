package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class TaskRepositoryImpl extends AbstractEventTaskEntityRepository<Task> implements TaskRepository {

    @Override
    public Task get(UUID id) {
        assertGet(id);
        return get(Task.class, id);
    }

    @Override
    public Task get(UUID id, Set<String> properties) {
        assertGet(id);
        return get(Task.class, id, properties);
    }

    @Override
    @Transactional
    public Task save(Task entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return super.save(entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        return delete(Task.class, id);
    }

    @Override
    public boolean delete(Task entity) {
        return false;
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
    public Collection<Task> addSubtasks(UUID scheduleId, Collection<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "event");
        Task schedule = get(scheduleId);
        schedule.getChildren().addAll(children);
        return save(schedule, userId).getChildren();
    }

    @Override
    public Collection<Task> removeSubtasks(UUID scheduleId, Collection<Task> children, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "event");
        Task task = get(scheduleId);
        Collection<Task> result = new ArrayList<>();
        for (Task event : task.getChildren()) {
            boolean foundToRemove = false;
            for (Task toRemove : children) {
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
