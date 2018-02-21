package timywimy.model.bo.events.extensions;

import timywimy.model.bo.events.extensions.common.AbstractDefaultEventExtension;

import javax.persistence.*;


@Entity
@Table(name = "bo_event_ext_tickboxes", indexes = {
        @Index(name = "bo_event_ext_tickboxes_idx_event_order", columnList = "event_id,event_order")})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TickBoxExtension extends AbstractDefaultEventExtension {

    @Column(name = "active", columnDefinition = "boolean", nullable = false)
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
