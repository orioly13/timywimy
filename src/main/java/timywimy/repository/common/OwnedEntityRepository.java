package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.security.User;

import java.util.Collection;

public interface OwnedEntityRepository<T extends AbstractOwnedEntity> extends EntityRepository<T> {

    Collection<T> getAllByOwner(User owner);
}
