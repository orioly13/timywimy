package com.timywimy.model.bo.events.extensions.common;

public interface OrderedEventExtension extends EventExtension {

    int getOrder();

    void setOrder(int order);
}
