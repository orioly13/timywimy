package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.NamedEventExtension;

public interface TickBoxExtension extends NamedEventExtension {

    boolean isActive();

    void setActive(boolean active);

}
