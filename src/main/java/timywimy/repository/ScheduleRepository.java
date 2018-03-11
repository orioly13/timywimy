package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.repository.common.OwnedEntityRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends OwnedEntityRepository<Schedule> {

    List<Event> addInstances(UUID scheduleId, List<Event> instances, UUID userId);

    List<Event> updateInstances(UUID scheduleId, List<Event> instances, UUID userId);

    List<Event> deleteInstances(UUID scheduleId, List<Event> instances, UUID userId);
}
