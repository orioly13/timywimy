package timywimy.model.bo.events.extensions;

import timywimy.model.bo.events.extensions.common.AbstractEventExtension;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "bo_event_ext_counters", indexes = {
        @Index(name = "bo_event_ext_counters_idx_event_order", columnList = "event_id,event_order")})
public class CounterExtension extends AbstractEventExtension {

    @Column(name = "counter", columnDefinition = "integer", nullable = false)
    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
