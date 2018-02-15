package com.timywimy.model.bo.events.extensions.common;

public abstract class OrderedEventExtensionImpl extends EventExtensionImpl implements OrderedEventExtension {

    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
