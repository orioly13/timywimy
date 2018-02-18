package com.timywimy.service;

import com.timywimy.model.common.BaseEntity;
import com.timywimy.model.security.User;

import java.util.List;
import java.util.UUID;

public interface AbstractService<T extends BaseEntity> {

    boolean create(T entity, UUID createdBy);

    T get(UUID id, UUID user);

    T update(T entity, UUID updatedBy);

    boolean delete(T entity, UUID deletedBy);

    List<T> getAll(UUID user);
}
