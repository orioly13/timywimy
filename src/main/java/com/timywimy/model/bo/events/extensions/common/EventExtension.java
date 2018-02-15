package com.timywimy.model.bo.events.extensions.common;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.BaseEntityImpl;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class EventExtension extends BaseEntityImpl {

    private Event event;

    protected EventExtension() {
    }

    protected EventExtension(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    //other methods

    @Override
    public String toString() {
        return String.format("(%s, event:%s)", getId(), (event == null ? "" : event.getId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof EventExtension)) {
            return false;
        }
        EventExtension that = (EventExtension) o;
        return Objects.equals(event, that.event) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, super.hashCode());
    }
}
