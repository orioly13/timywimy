package com.timywimy.model.bo.events.extensions.common;

import com.timywimy.model.bo.events.Event;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class NamedEventExtension extends OrderedEventExtension {


    private String name;

    protected NamedEventExtension() {
    }

    protected NamedEventExtension(Event event, int order, String name) {
        super(event, order);
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
        if (o == null || !(o instanceof NamedEventExtension)) {
            return false;
        }
        NamedEventExtension that = (NamedEventExtension) o;
        return Objects.equals(name, that.name) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, super.hashCode());
    }
}
