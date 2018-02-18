package timywimy.model.common;

import timywimy.model.security.User;

public interface OwnedEntity extends BaseEntity {

    User getOwner();

    void setOwner(User owner);
}
