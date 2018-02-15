package com.timywimy.model.bo.events.extensions.common;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.BaseEntity;

public interface EventExtension extends BaseEntity {

    Event getEvent();

    void setEvent(Event event);
}
