package timywimy.model.bo.events.extensions;

import timywimy.model.bo.events.extensions.common.AbstractEventExtension;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Table(name = "bo_event_ext_tickboxes", indexes = {
        @Index(name = "bo_event_ext_tickboxes_idx_event_order", columnList = "event_id,event_order")})
public class TickBoxExtension extends AbstractEventExtension {

    @Column(name = "active", columnDefinition = "boolean")
    private Boolean active;

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
