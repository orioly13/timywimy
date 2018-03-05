package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.Collection;
import java.util.UUID;

public interface EventRepository extends EventTaskEntityRepository<Event> {

    Collection<AbstractEventExtension> addEventExtensions(UUID eventId, Collection<AbstractEventExtension> eventExtensions, UUID userId);

    Collection<AbstractEventExtension> removeEventExtension(UUID eventId, Collection<AbstractEventExtension> eventExtensions, UUID userId);
}
