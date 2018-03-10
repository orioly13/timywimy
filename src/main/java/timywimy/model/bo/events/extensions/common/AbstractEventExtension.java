package timywimy.model.bo.events.extensions.common;

import timywimy.model.bo.events.Event;
import timywimy.model.common.AbstractBaseEntity;
import timywimy.model.common.OrderedEntity;

import javax.persistence.*;

//HAD TO ADD ENTITY AND INHERITANCE (otherwise ManyToOne causes not mapped exception)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEventExtension extends AbstractBaseEntity implements OrderedEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Event.class)
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "event_order", columnDefinition = "integer")
    private int order;


    public Event getEvent() {
        return event;
    }


    public void setEvent(Event event) {
        this.event = event;
    }


    public int getOrder() {
        return order;
    }


    public void setOrder(int order) {
        this.order = order;
    }
}
