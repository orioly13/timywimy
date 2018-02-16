package com.timywimy.model.bo.events;

import com.timywimy.model.common.DescribedEntity;
import com.timywimy.model.common.DurableEntity;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;

public interface Schedule extends OwnedEntity, NamedEntity, DescribedEntity, DurableEntity {

    String getCron();

    void setCron(String cron);

    List<Event> getInstances();

    void setInstances(List<Event> instances);
}
