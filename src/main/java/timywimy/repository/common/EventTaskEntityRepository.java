package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.common.DateTimeZone;
import timywimy.model.security.User;

import java.util.Collection;

public interface EventTaskEntityRepository<T extends AbstractOwnedEntity> extends OwnedEntityRepository<T> {

    Collection<T> getByOwnerBetween(User owner, DateTimeZone start, DateTimeZone end);
}
