package timywimy.model.bo.events.extensions;


import timywimy.model.bo.events.extensions.common.EventExtension;
import timywimy.model.common.NamedEntity;
import timywimy.model.common.OrderedEntity;

public interface CounterExtension extends NamedEntity, OrderedEntity, EventExtension {

    int getCounter();

    void setCounter(int counter);
}
