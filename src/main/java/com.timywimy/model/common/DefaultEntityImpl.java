package com.timywimy.model.common;

import com.timywimy.model.security.User;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultEntityImpl extends BaseEntityImpl implements OwnedEntity, NamedEntity, DescribedEntity {

    private User owner;
    private String name;
    private String description;

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
