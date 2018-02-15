package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.NamedEventExtensionImpl;

import java.util.Objects;

public class CounterExtensionImpl extends NamedEventExtensionImpl implements CounterExtension {

    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

}
