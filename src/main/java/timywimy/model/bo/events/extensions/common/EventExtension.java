package timywimy.model.bo.events.extensions.common;

import timywimy.model.bo.events.Event;
import timywimy.model.common.BaseEntity;

public interface EventExtension extends BaseEntity {

    Event getEvent();

    void setEvent(Event event);
}
