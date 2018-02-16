package com.timywimy.model.bo.events.extensions;


import com.timywimy.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OrderedEntity;

public interface CounterExtension extends NamedEntity, OrderedEntity, EventExtension {

    int getCounter();

    void setCounter(int counter);
}
