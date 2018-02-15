package com.timywimy.model.bo.events.extensions.common;

public interface NamedEventExtension extends OrderedEventExtension {

    String getName();

    void setName(String name);
}
