package timywimy.web.controllers.entities;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface EntityController<T extends BaseEntity> {

    T get(UUID id, UUID session);

    T save(T entity, UUID session);

    boolean delete(UUID id, UUID session);

    List<T> getAll(UUID session);
}
