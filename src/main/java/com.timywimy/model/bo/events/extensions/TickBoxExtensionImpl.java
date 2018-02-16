package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.DefaultEventExtensionImpl;

import javax.persistence.Entity;

@Entity
public class TickBoxExtensionImpl extends DefaultEventExtensionImpl implements TickBoxExtension {

    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
