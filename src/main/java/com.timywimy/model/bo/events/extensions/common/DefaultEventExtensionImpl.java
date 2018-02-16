package com.timywimy.model.bo.events.extensions.common;

import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OrderedEntity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultEventExtensionImpl extends EventExtensionImpl implements NamedEntity, OrderedEntity {

    private String name;
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
