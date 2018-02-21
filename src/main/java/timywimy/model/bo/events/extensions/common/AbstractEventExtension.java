package timywimy.model.bo.events.extensions.common;

import timywimy.model.bo.events.Event;
import timywimy.model.common.AbstractBaseEntity;

import javax.persistence.*;

//HAD TO ADD ENTITY AND INHERITANCE (otherwise ManyToOne causes not mapped exception)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEventExtension extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Event.class)
    @JoinColumn(name = "event_id")
    private Event event;


    public Event getEvent() {
        return event;
    }


    public void setEvent(Event event) {
        this.event = event;
    }
}
