package com.timywimy.model.bo.events.extensions;


import com.timywimy.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OrderedEntity;

public interface TickBoxExtension extends NamedEntity, OrderedEntity, EventExtension {

    boolean isActive();

    void setActive(boolean active);

}
