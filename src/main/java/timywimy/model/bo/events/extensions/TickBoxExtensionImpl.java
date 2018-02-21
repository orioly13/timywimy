package timywimy.model.bo.events.extensions;

import timywimy.model.bo.events.extensions.common.DefaultEventExtensionImpl;
import timywimy.model.common.NamedEntity;
import timywimy.model.common.OrderedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


//@Entity
//@Table(name = "bo_event_ext_tickboxes", indexes = {
//        @Index(name = "bo_event_ext_tickboxes_idx_event_order", columnList = "event_id,event_order")})
public class TickBoxExtensionImpl extends DefaultEventExtensionImpl{

    @Column(name = "active", columnDefinition = "boolean", nullable = false)
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}