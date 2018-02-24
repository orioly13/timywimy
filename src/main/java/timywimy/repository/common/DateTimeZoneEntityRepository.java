package timywimy.repository.common;

import timywimy.model.common.DateTimeZone;
import timywimy.model.common.DateTimeZoneEntity;

import java.util.List;
import java.util.UUID;

public interface DateTimeZoneEntityRepository<T extends DateTimeZoneEntity> extends EntityRepository<T> {

    List<T> getBetweenOwned(DateTimeZone start, DateTimeZone end, UUID ownerId);
}
