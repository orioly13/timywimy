package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;

import java.util.List;

public interface EventTaskEntityRepository<T extends AbstractOwnedEntity> extends OwnedEntityRepository<T> {

    List<T> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end);
}
