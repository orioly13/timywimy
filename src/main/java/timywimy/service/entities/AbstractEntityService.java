package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.service.APIService;

public abstract class AbstractEntityService<T extends BaseEntity> implements EntityService<T> {

    protected final APIService apiService;

    @Autowired
    protected AbstractEntityService(APIService apiService) {
        Assert.notNull(apiService,"APIService should be provided");
        this.apiService = apiService;
    }
}
