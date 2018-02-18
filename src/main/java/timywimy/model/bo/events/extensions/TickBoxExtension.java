package timywimy.model.bo.events.extensions;


import timywimy.model.bo.events.extensions.common.EventExtension;
import timywimy.model.common.NamedEntity;
import timywimy.model.common.OrderedEntity;

public interface TickBoxExtension extends NamedEntity, OrderedEntity, EventExtension {

    boolean isActive();

    void setActive(boolean active);

}
