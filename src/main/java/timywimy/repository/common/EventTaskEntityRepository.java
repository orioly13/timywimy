package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.common.util.DateTimeZone;

import java.util.List;
import java.util.UUID;

public interface EventTaskEntityRepository<T extends AbstractOwnedEntity> extends OwnedEntityRepository<T> {

    List<T> getByOwnerBetween(UUID owner, DateTimeZone start, DateTimeZone end);
}
