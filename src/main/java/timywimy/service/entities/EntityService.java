package timywimy.service.entities;

import timywimy.model.common.BaseEntity;
import timywimy.model.security.User;

import java.util.List;
import java.util.UUID;

public interface EntityService<T extends BaseEntity> {

    T create(T entity, UUID userSession);

    T update(T entity, UUID userSession);

    T get(UUID id, UUID userSession);

    boolean delete(UUID id, UUID userSession);

    List<T> getAll(UUID userSession);
}
