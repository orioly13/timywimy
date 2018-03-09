package timywimy.repository;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.repository.common.EventTaskEntityRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends EventTaskEntityRepository<Event> {

    List<AbstractEventExtension> addExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<AbstractEventExtension> updateExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);

    List<AbstractEventExtension> deleteExtensions(UUID eventId, List<AbstractEventExtension> eventExtensions, UUID userId);
}
