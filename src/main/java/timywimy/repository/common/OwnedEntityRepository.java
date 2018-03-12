package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.security.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OwnedEntityRepository<T extends AbstractOwnedEntity> extends EntityRepository<T> {

    List<T> getAllByOwner(UUID owner);
}
