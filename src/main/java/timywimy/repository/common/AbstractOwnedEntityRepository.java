package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.util.RequestUtil;
import timywimy.util.exception.RepositoryException;

public abstract class AbstractOwnedEntityRepository<T extends AbstractOwnedEntity> extends AbstractEntityRepository{

    protected void assertOwner(AbstractOwnedEntity entity) {
        RequestUtil.validateEmptyField(RepositoryException.class, entity, "entity");
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getOwner(), "owner");
        RequestUtil.validateEmptyField(RepositoryException.class, entity.getOwner().getId(), "owner");
    }

//    protected Collection<T> getAllByOwner(Collection<T> ownedList) {
//        Collection<T> result = new ArrayList<>();
//        //            if (ownedEntity.getDeletedTs() == null) {
//        //            }
//        result.addAll(ownedList);
//        return result;
//    }
}
