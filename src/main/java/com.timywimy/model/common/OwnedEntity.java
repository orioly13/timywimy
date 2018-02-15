package com.timywimy.model.common;

import com.timywimy.model.security.User;

public interface OwnedEntity extends BaseEntity {

    User getOwner();

    void setOwner(User owner);
}
