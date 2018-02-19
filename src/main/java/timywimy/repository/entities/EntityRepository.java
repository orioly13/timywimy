package timywimy.repository.entities;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T extends BaseEntity> {

    T get(UUID id);

    T save(T entity, UUID updatedBy);

    boolean delete(UUID id, UUID deletedBy);

    List<T> getAll();
}
