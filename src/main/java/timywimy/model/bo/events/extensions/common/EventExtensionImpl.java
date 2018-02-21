package timywimy.model.bo.events.extensions.common;

import timywimy.model.bo.events.EventImpl;
import timywimy.model.common.AbstractBaseEntity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

//@MappedSuperclass
public abstract class EventExtensionImpl extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventImpl event;


    public EventImpl getEvent() {
        return event;
    }


    public void setEvent(EventImpl event) {
        this.event = event;
    }
}
