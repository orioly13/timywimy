package com.timywimy.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DescribedEntityImpl extends NamedEntityImpl implements DescribedEntity {

    @Column(name = "description", columnDefinition = "VARCHAR", nullable = false)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}