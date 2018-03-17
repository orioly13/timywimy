package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import timywimy.model.common.BaseEntity;
import timywimy.model.security.converters.Role;
import timywimy.repository.common.EntityRepository;
import timywimy.service.RestService;
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

    public List<E> getAllEntities(UUID userSession) {
        assertUserRole(getUserBySession(userSession).getRole());

        return repository.getAll();
    }

}
