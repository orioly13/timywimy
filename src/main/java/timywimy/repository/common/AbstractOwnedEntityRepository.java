package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractOwnedEntityRepository<T extends AbstractOwnedEntity> extends AbstractEntityRepository<T>{

    protected void assertOwner(AbstractOwnedEntity entity) {
        RequestUtil.validateEmptyField(RepositoryException.class, entity, "entity");
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getOwner(), "owner");
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getOwner().getId(), "owner");
    }

    protected Collection<T> getAllByOwner(Collection<T> ownedList) {
        Collection<T> result = new ArrayList<>();
        for (T ownedEntity : ownedList) {
            if (ownedEntity.getDeletedTs() == null) {
                result.add(ownedEntity);
            }
        }
        return result;
    }
}
