package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import timywimy.model.common.BaseEntity;
import timywimy.model.security.User;
import timywimy.repository.common.EntityRepository;
import timywimy.service.RestService;

import java.util.UUID;

public abstract class AbstractEntityService<T extends BaseEntity> implements EntityService<T> {

    protected final RestService restService;
    protected final EntityRepository<T> repository;

    @Autowired
    protected AbstractEntityService(RestService restService, EntityRepository<T> repository) {
        Assert.notNull(restService, "APIService should be provided");
        Assert.notNull(repository, "Entity repository should be provided");
        this.restService = restService;
        this.repository = repository;
    }

    protected User getUserBySession(UUID session) {
        return restService.getUserBySession(session);
    }

}
