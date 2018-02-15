package com.timywimy.model.model.bo.events.extensions.common;

import com.timywimy.model.model.bo.events.Event;

import java.util.Objects;

public abstract class OrderedEventExtension extends EventExtension {

    private int order;

    protected OrderedEventExtension() {
    }

    protected OrderedEventExtension(Event event, int order) {
        super(event);
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    //other methods

    @Override
    public String toString() {
        return String.format("(%s, order:%s)", super.toString(), order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof OrderedEventExtension)) {
            return false;
        }
        OrderedEventExtension that = (OrderedEventExtension) o;
        return order == that.order && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, super.hashCode());
    }
}
