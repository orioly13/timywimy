package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.NamedEventExtension;

import java.util.Objects;

public class CounterExtension extends NamedEventExtension {

    private int counter;

    public CounterExtension() {
    }

    public CounterExtension(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    //other methods
    @Override
    public String toString() {
        return String.format("Counter(%s, counter:%s)", super.toString(), counter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof CounterExtension)) {
            return false;
        }
        CounterExtension that = (CounterExtension) o;
        return counter == that.counter && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counter, super.hashCode());
    }

}
