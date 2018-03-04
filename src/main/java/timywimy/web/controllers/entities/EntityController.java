package timywimy.web.controllers.entities;


import timywimy.web.dto.common.Response;

import java.util.List;
import java.util.UUID;

public interface EntityController<T> {

    Response<T> get(Integer requestId, UUID session, UUID entityId);

    Response<T> create(Integer requestId, UUID session, T entity);

    Response<T> update(Integer requestId, UUID session, UUID entityId, T entity);

    Response<Boolean> delete(Integer requestId, UUID session, UUID entityId);

    Response<List<T>> getAll(Integer requestId, UUID session);
}
