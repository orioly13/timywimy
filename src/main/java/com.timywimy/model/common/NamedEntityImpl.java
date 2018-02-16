package com.timywimy.model.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEntityImpl extends BaseEntityImpl implements NamedEntity {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
