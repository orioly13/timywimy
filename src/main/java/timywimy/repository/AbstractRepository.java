package timywimy.repository;

import timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface AbstractRepository<T extends BaseEntity> {

    //todo check role
    //todo check owner
    boolean create(T entity);

    T get(UUID id);

    T update(T entity);

    boolean delete(UUID id);

    List<T> getAll();
}
