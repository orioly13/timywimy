package com.timywimy.model.model.bo.events.extensions;

import com.timywimy.model.model.bo.events.extensions.common.NamedEventExtension;

import java.util.Objects;

public class TickBoxExtension extends NamedEventExtension {

    private boolean active;

    public TickBoxExtension() {
    }

    public TickBoxExtension(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //other methods
    @Override
    public String toString() {
        return String.format("TickBox(%s, active:%s)", super.toString(), active);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof TickBoxExtension)) {
            return false;
        }
        TickBoxExtension that = (TickBoxExtension) o;
        return active == that.active && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, super.hashCode());
    }
}
