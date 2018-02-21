package timywimy.model.bo.events.extensions;

import timywimy.model.bo.events.extensions.common.AbstractDefaultEventExtension;

import javax.persistence.*;

@Entity
@Table(name = "bo_event_ext_counters", indexes = {
        @Index(name = "bo_event_ext_counters_idx_event_order", columnList = "event_id,event_order")})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CounterExtension extends AbstractDefaultEventExtension {

    @Column(name = "counter", columnDefinition = "integer", nullable = false)
    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
