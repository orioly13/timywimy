package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.security.User;

import java.util.Collection;
import java.util.List;

public interface OwnedEntityRepository<T extends AbstractOwnedEntity> extends EntityRepository<T> {

    List<T> getAllByOwner(User owner);
}
