package timywimy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.security.User;
import timywimy.repository.common.AbstractOwnedEntityRepository;
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
public class ScheduleRepositoryImpl extends AbstractOwnedEntityRepository<Schedule> implements ScheduleRepository {

    @Override
    public Schedule get(UUID id) {
        assertGet(id);
        return get(Schedule.class, id);
    }

    @Override
    @Transactional
    public Schedule save(Schedule entity, UUID userId) {
        assertSave(entity, userId);
        assertOwner(entity);
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getName(), "user name");
        return save(Schedule.class, entity, userId);
    }

    @Override
    @Transactional
    public boolean delete(UUID id, UUID userId) {
        assertDelete(id, userId);
        return delete(Schedule.class, id, userId);
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
    public Collection<Schedule> getAllByOwner(User owner) {
        RequestUtil.validateEmptyField(RepositoryException.class, owner, "user");
        return owner.getSchedules();
    }

    @Override
    public Collection<Event> addScheduleInstances(UUID scheduleId, Collection<Event> instances, UUID userId) {
        //todo validate cron before adding
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "event");
        Schedule schedule = get(scheduleId);
        schedule.getInstances().addAll(instances);
        return save(schedule, userId).getInstances();
    }

    @Override
    public Collection<Event> removeScheduleInstances(UUID scheduleId, Collection<Event> instances, UUID userId) {
        RequestUtil.validateEmptyField(RepositoryException.class, scheduleId, "event");
        Schedule schedule = get(scheduleId);
        Collection<Event> result = new ArrayList<>();
        for (Event event : schedule.getInstances()) {
            boolean foundToRemove = false;
            for (Event toRemove : instances) {
                if (event.getId().equals(toRemove.getId())) {
                    foundToRemove = true;
                    break;
                }
            }
            if (!foundToRemove) {
                result.add(event);
            }
        }
        schedule.setInstances(result);
        return save(schedule, userId).getInstances();
    }
}
