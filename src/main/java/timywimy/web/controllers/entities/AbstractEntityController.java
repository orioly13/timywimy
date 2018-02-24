package timywimy.web.controllers.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.service.entities.EntityService;

import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityController<T extends BaseEntity> implements EntityController<T> {
    protected final Logger log;
    protected final EntityService<T> service;

    @Autowired
    protected AbstractEntityController(EntityService<T> service, Logger log) {
        Assert.notNull(service, "Entity service should be provided");
        Assert.notNull(service, "Logger  should be provided");
        this.service = service;
        this.log=log;
    }


    public T get(Class<T> entityClass, UUID entityId, UUID userSession) {
        Assert.notNull(entityClass, "entity class must not be null");
        Assert.notNull(entityId, "entity id must not be null");
        Assert.notNull(userSession, "user session must not be null");
        return service.get(entityId, userSession);
    }

    public T save(Class<T> entityClass, T entity, UUID userSession) {
        Assert.notNull(entityClass, "entity class must not be null");
        Assert.notNull(entity, "entity must not be null");
        Assert.notNull(userSession, "user session must not be null");

        return service.save(entity, userSession);
    }

    public boolean delete(Class<T> entityClass, UUID entityId, UUID userSession) {
        Assert.notNull(entityClass, "entity calss must not be null");
        Assert.notNull(entityId, "entity id must not be null");
        Assert.notNull(userSession, "user session must not be null");

        return service.delete(entityId, userSession);
    }

    public List<T> getAll(Class<T> entityClass, UUID userSession) {
        Assert.notNull(entityClass, "entity calss must not be null");
        Assert.notNull(userSession, "user session must not be null");

        return service.getAll(userSession);
    }

}
