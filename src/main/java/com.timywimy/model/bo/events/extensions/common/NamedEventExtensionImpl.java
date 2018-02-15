package com.timywimy.model.bo.events.extensions.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEventExtensionImpl extends OrderedEventExtensionImpl implements NamedEventExtension {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
