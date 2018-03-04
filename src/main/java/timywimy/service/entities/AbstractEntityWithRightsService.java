package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.common.EntityRepository;
import timywimy.service.RestService;

import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityWithRightsService<T extends BaseEntity> extends AbstractEntityService<T> {

    protected List<Role> roles;

    @Autowired
    protected AbstractEntityWithRightsService(RestService restService, EntityRepository<T> repository) {
        super(restService, repository);
    }

    protected void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    protected void assertUserRole(Role role) {
        if (roles.contains(role)) {
            throw new IllegalArgumentException("not enough rights");
        }
    }

    public T get(Class<T> entityClass, UUID entityId, UUID userSession) {
        Assert.notNull(entityClass, "entity calss must not be null");
        Assert.notNull(entityId, "entity id must not be null");
        Assert.notNull(userSession, "user session must not be null");
        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.get(entityId);
    }

    public T save(Class<T> entityClass, T entity, UUID userSession) {
        Assert.notNull(entityClass, "entity class must not be null");
        Assert.notNull(entity, "entity must not be null");
        Assert.notNull(userSession, "user session must not be null");
        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.save(entity, userBySession.getId());
    }

    public boolean delete(Class<T> entityClass, UUID entityId, UUID userSession) {
        Assert.notNull(entityClass, "entity calss must not be null");
        Assert.notNull(entityId, "entity id must not be null");
        Assert.notNull(userSession, "user session must not be null");
        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.delete(entityId, userBySession.getId());
    }

    public List<T> getAll(Class<T> entityClass, UUID userSession) {
        Assert.notNull(entityClass, "entity calss must not be null");
        Assert.notNull(userSession, "user session must not be null");
        User userBySession = getUserBySession(userSession);
        assertUserRole(userBySession.getRole());

        return repository.getAll();
    }

}
