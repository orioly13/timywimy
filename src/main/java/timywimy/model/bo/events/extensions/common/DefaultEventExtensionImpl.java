package timywimy.model.bo.events.extensions.common;

import timywimy.model.common.NamedEntity;
import timywimy.model.common.OrderedEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultEventExtensionImpl extends EventExtensionImpl implements NamedEntity, OrderedEntity {

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;
    @Column(name = "event_order", columnDefinition = "integer")
    private int order;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }
}
