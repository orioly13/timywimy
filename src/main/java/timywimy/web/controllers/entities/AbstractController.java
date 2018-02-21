package timywimy.web.controllers.entities;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface AbstractController<T extends BaseEntity> {

    T create(T entity, UUID session);

    T get(UUID id, UUID session);

    T update(T entity, UUID session);

    boolean delete(UUID id, UUID session);

    List<T> getAll(UUID session);
}
