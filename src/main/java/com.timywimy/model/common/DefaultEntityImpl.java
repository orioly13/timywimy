package com.timywimy.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultEntityImpl extends OwnedEntityImpl implements NamedEntity, DescribedEntity {

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;
    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

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
