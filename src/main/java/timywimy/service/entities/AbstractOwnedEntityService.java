package timywimy.service.entities;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.security.User;
import timywimy.repository.common.EntityRepository;
import timywimy.service.RestService;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;

public abstract class AbstractOwnedEntityService<T, E extends AbstractOwnedEntity> extends AbstractEntityService<T, E> {

    protected AbstractOwnedEntityService(RestService restService, EntityRepository<E> repository) {
        super(restService, repository);
    }

    protected<K extends AbstractOwnedEntity> void assertOwner(K entity, User user) {
        if (!entity.getOwner().getId().equals(user.getId())) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS);
        }
    }
}