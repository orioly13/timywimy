package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.DefaultEventExtensionImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "bo_event_ext_counters", indexes = {
        @Index(name = "bo_event_ext_counters_idx_event_order", columnList = "event_id,event_order")})
public class CounterExtensionImpl extends DefaultEventExtensionImpl implements CounterExtension {

    @Column(name = "counter", columnDefinition = "integer", nullable = false)
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
