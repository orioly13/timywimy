package com.timywimy.model.bo.events.extensions.common;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.BaseEntityImpl;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EventExtensionImpl extends BaseEntityImpl implements EventExtension {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
    }
}
