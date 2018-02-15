package com.timywimy.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEntityImpl extends BaseEntityImpl implements NamedEntity {

    @Column(name = "name", columnDefinition = "VARCHAR", nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
