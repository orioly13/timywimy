package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.security.User;
import timywimy.repository.common.DateTimeZoneEntityRepository;
import timywimy.repository.common.EntityRepository;

public interface EventRepository extends DateTimeZoneEntityRepository<Event> {

}
