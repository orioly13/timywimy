package com.timywimy.model.bo.events;

import com.timywimy.model.common.DurableEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;

public interface Schedule extends DurableEntity, OwnedEntity {

    String getCron();

    void setCron();

    List<Event> getInstances();

    void setInstances(List<Event> instances);
}
