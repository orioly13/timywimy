package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import timywimy.model.common.BaseEntity;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.common.EntityRepository;
import timywimy.service.RestService;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;

import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityWithRightsService<T, E extends BaseEntity> extends AbstractEntityService<T, E> {

    protected List<Role> roles;

    @Autowired
    protected AbstractEntityWithRightsService(RestService restService, EntityRepository<E> repository) {
        super(restService, repository);
    }

    protected void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    protected void assertUserRole(Role role) {
        if (!roles.contains(role)) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS);
        }
    }

    protected E getEntity(UUID entityId, UUID userSession) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(entityId, "entity id"),
                new PairFieldName<>(userSession, "session"));
        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.get(entityId);
    }

    protected E saveEntity(E entity, UUID userSession) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(entity, "entity"),
                new PairFieldName<>(userSession, "session"));

        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.save(entity, userBySession.getId());
    }

    protected boolean deleteEntity(UUID entityId, UUID userSession) {
        RequestUtil.validateEmptyFields(ServiceException.class,
                new PairFieldName<>(entityId, "entity id"),
                new PairFieldName<>(userSession, "session"));

        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.delete(entityId);
    }

    public List<E> getAllEntities(UUID userSession) {
        RequestUtil.validateEmptyField(ServiceException.class, userSession, "user session must not be null");

        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.getAll();
    }

}
