package timywimy.service.entities;

import timywimy.model.common.BaseEntity;
import timywimy.service.APIService;

public abstract class AbstractEntityService<T extends BaseEntity> implements EntityService<T> {

    protected final APIService apiService;

    protected AbstractEntityService(APIService apiService) {
        this.apiService = apiService;
    }
}
