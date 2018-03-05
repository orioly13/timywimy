package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.repository.common.OwnedEntityRepository;

import java.util.Collection;
import java.util.UUID;

public interface ScheduleRepository extends OwnedEntityRepository<Schedule> {

    Collection<Event> addScheduleInstances(UUID scheduleId, Collection<Event> instances, UUID userId);

    Collection<Event> removeScheduleInstances(UUID scheduleId, Collection<Event> instances, UUID userId);
}
