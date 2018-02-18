package com.timywimy.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEntityImpl extends BaseEntityImpl implements NamedEntity {

    @Column(name = "name", columnDefinition = "varchar(50)")
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
