package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.service.entities.EntityService;
import timywimy.util.PairFieldName;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ControllerException;
import timywimy.web.dto.common.Request;

import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityController<T, E extends BaseEntity> implements EntityController<T> {
    protected final EntityService<T,E> service;

    @Autowired
    protected AbstractEntityController(EntityService<T,E> service) {
        Assert.notNull(service, "Entity service should be provided");
        this.service = service;
    }

    public T get(UUID entityId, UUID userSession) {
        return service.get(entityId, userSession);
    }

    public T save(T entity, UUID userSession) {
        return service.save(entity, userSession);
    }

    public boolean delete(UUID entityId, UUID userSession) {
        return service.delete(entityId, userSession);
    }

    public List<T> getAll(UUID userSession) {
        return service.getAll(userSession);
    }

}
