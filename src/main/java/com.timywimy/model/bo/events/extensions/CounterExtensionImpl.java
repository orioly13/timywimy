package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.DefaultEventExtensionImpl;

import javax.persistence.Entity;

@Entity
public class CounterExtensionImpl extends DefaultEventExtensionImpl implements CounterExtension {

    private int counter;

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
    }
}
