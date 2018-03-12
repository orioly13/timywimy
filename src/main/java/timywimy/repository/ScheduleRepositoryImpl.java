package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.model.bo.tasks.Task;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractOwnedEntityRepository;
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
public class ScheduleRepositoryImpl extends AbstractOwnedEntityRepository<Schedule> implements ScheduleRepository {


    @Override
    public Schedule get(UUID id) {
        assertGet(id);
        return getBaseEntity(Schedule.class, id);
    }

    @Override
    public Schedule get(UUID id, Set<String> properties) {
        assertGet(id);
        return getBaseEntity(Schedule.class, id, properties);
    }

    @Override
    @Transactional
    public Schedule save(Schedule entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return saveBaseEntity(entity, userId);
    }

    private void deleteInstances(Schedule schedule) {
        List<Event> events = schedule.getInstances();
        if (events.size() > 0) {
            for (Event event : events) {
                List<Task> tasks = event.getTasks();
                for (Task task : tasks) {
                    task.setEvent(null);
                    entityManager.merge(task);
                }
                entityManager.remove(event);
            }
            entityManager.flush();
        }
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        assertDelete(id);
        deleteInstances(get(id, constructParametersSet("instances")));
        return deleteBaseEntity(Schedule.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Schedule schedule) {
        RequestUtil.validateEmptyField(RepositoryException.class, schedule, "schedule");
        assertDelete(schedule.getId());
        deleteInstances(get(schedule.getId(), constructParametersSet("instances")));
        return deleteBaseEntity(Schedule.class, schedule);
    }

    @Override
    public List<Schedule> getAll() {
        CriteriaQuery<Schedule> criteria = builder.createQuery(Schedule.class);
        Root<Schedule> userRoot = criteria.from(Schedule.class);
        criteria.select(userRoot).
                orderBy(builder.asc(userRoot.get("owner.id")),
                        builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<Schedule> getAllByOwner(UUID owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        CriteriaQuery<Schedule> criteria = builder.createQuery(Schedule.class);
        Root<Schedule> userRoot = criteria.from(Schedule.class);
        criteria.select(userRoot).
                where(builder.equal(userRoot.get("owner.id"), owner)).
                orderBy(builder.asc(userRoot.get("dateTimeZone")));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    @Transactional
    public List<Event> addInstances(UUID scheduleId, List<Event> instances, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "schedule");
        RequestUtil.validateEmptyField(RepositoryException.class, instances, "instances");
        Schedule schedule = get(scheduleId);
        RequestUtil.validateEmptyField(RepositoryException.class, schedule, "schedule");
        List<Event> scheduleInstances = schedule.getInstances();
        for (Event toAdd : instances) {
            RequestUtil.validateEmptyField(RepositoryException.class, toAdd, "schedule instance");

            toAdd.setSchedule(schedule);
            persistEntity(userId, toAdd);
            scheduleInstances.add(toAdd);

        }

        return get(scheduleId, constructParametersSet("instances")).getInstances();
    }

    @Override
    @Transactional
    public List<Event> updateInstances(UUID scheduleId, List<Event> instances, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "schedule");
        RequestUtil.validateEmptyField(RepositoryException.class, instances, "instances");
        Schedule schedule = get(scheduleId);
        RequestUtil.validateEmptyField(RepositoryException.class, schedule, "schedule");

        for (Event toUpdate : instances) {
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate, "shed event");
            RequestUtil.validateEmptyField(RepositoryException.class, toUpdate.getId(), "shed event id");

            boolean foundToUpdate = false;
            //without transaction proxy can't be initialized
            for (Event instance : schedule.getInstances()) {
                foundToUpdate = isFoundToUpdate(userId, toUpdate, instance);
                if (foundToUpdate) {
                    toUpdate.setSchedule(schedule);
                    entityManager.merge(toUpdate);
                    break;
                }
            }

            if (!foundToUpdate) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to update instances that don't exist in schedule");
            }
        }

        return get(scheduleId, constructParametersSet("instances")).getInstances();
    }

    @Override
    @Transactional
    public List<Event> deleteInstances(UUID scheduleId, List<Event> instances, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "schedule");
        RequestUtil.validateEmptyField(RepositoryException.class, instances, "instances");
        Schedule schedule = get(scheduleId);
        RequestUtil.validateEmptyField(RepositoryException.class, schedule, "schedule");

        for (Event toDelete : instances) {
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete, "shed event");
            RequestUtil.validateEmptyField(RepositoryException.class, toDelete.getId(), "shed event id");

            if (!isFoundToDelete(toDelete, schedule.getInstances())) {
                throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS,
                        "Trying to delete instances that don't exist in schedule");
            }
        }

        return get(scheduleId, constructParametersSet("instances")).getInstances();
    }
}
