package timywimy.model.bo.events;

import timywimy.model.common.DescribedEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.NamedEntity;
import timywimy.model.common.OwnedEntity;

import java.util.List;

public interface Schedule extends OwnedEntity, NamedEntity, DescribedEntity, DurableEntity {

    String getCron();

    void setCron(String cron);

    List<Event> getInstances();

    void setInstances(List<Event> instances);
}
