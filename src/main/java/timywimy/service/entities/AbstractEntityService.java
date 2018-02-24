package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.model.security.User;
import timywimy.repository.common.EntityRepository;
import timywimy.service.APIService;

import java.util.UUID;

public abstract class AbstractEntityService<T extends BaseEntity> implements EntityService<T> {

    protected final APIService apiService;
    protected final EntityRepository<T> repository;

    @Autowired
    protected AbstractEntityService(APIService apiService, EntityRepository<T> repository) {
        Assert.notNull(apiService, "APIService should be provided");
        Assert.notNull(repository, "Entity repository should be provided");
        this.apiService = apiService;
        this.repository = repository;
    }

    protected User getUserBySession(UUID session) {
        return apiService.getUserBySession(session);
    }

}
