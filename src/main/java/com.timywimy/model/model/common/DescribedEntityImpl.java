package com.timywimy.model.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class DescribedEntityImpl extends NamedEntityImpl implements DescribedEntity {

    @Column(name = "description", columnDefinition = "VARCHAR", nullable = false)
    private String description;


    protected DescribedEntityImpl() {
    }

    protected DescribedEntityImpl(String name, String description, UUID id) {
        super(name, id);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //other methods

    @Override
    public String toString() {
        return String.format("(%s, desc:%s)", super.toString(), description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DescribedEntity)) {
            return false;
        }
        DescribedEntityImpl that = (DescribedEntityImpl) o;
        return Objects.equals(description, that.description) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, super.hashCode());
    }
}