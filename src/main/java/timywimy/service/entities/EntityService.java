package timywimy.service.entities;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface EntityService<T, E extends BaseEntity> {

    T get(UUID entityId, UUID userSession);

    T save(T dto, UUID userSession);

    boolean delete(UUID entityId, UUID userSession);

    List<T> getAll(UUID userSession);
}
