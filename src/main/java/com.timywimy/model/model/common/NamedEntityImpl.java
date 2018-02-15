package com.timywimy.model.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class NamedEntityImpl extends BaseEntityImpl implements NamedEntity {

    @Column(name = "name", columnDefinition = "VARCHAR", nullable = false)
    private String name;


    protected NamedEntityImpl() {
    }

    protected NamedEntityImpl(String name, UUID id) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //other methods

    @Override
    public String toString() {
        return String.format("(%s, name:%s)", super.toString(), name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof NamedEntity)) {
            return false;
        }
        NamedEntityImpl that = (NamedEntityImpl) o;
        return Objects.equals(name, that.name) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, super.hashCode());
    }
}
