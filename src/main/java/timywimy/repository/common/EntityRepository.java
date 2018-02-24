package timywimy.repository.common;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T extends BaseEntity> {

    T get(UUID id);

    T save(T entity, UUID userId);

    boolean delete(UUID id, UUID userId);

    List<T> getAll();
}
