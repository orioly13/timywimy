package com.timywimy.web;

import com.timywimy.model.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface AbstractController<T extends BaseEntity> {

    boolean create(T entity, UUID createdBy);

    T get(UUID id, UUID user);

    T update(T entity, UUID updatedBy);

    boolean delete(T entity, UUID deletedBy);

    List<T> getAll(UUID user);
}
