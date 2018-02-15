package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.NamedEventExtensionImpl;

public class TickBoxExtensionImpl extends NamedEventExtensionImpl implements TickBoxExtension {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
