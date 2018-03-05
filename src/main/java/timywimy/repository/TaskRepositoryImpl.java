package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractEventTaskEntityRepository;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class TaskRepositoryImpl extends AbstractEventTaskEntityRepository<Task> implements TaskRepository {

    @Override
    public Task get(UUID id) {
        assertGet(id);
        return get(Task.class, id);
    }

    @Override
    @Transactional
    public Task save(Task entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return save(Task.class, entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID userId) {
        assertDelete(id, userId);
        return delete(Task.class, id, userId);
    }

    @Override
    public List<Task> getAll() {
        CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
        Root<Task> userRoot = criteria.from(Task.class);
        criteria.select(userRoot).where(getDeletedTsExpression(userRoot, null))
                .orderBy(builder.asc(userRoot.get("owner.id")), builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Collection<Task> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return getAllByOwner(owner.getTasks());
    }

    @Override
    public Collection<Task> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        Collection<Task> allByOwner = getAllByOwner(owner);
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
