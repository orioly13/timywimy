package com.timywimy.model.bo.events.extensions;

import com.timywimy.model.bo.events.extensions.common.NamedEventExtension;

public interface CounterExtension extends NamedEventExtension {

    int getCounter();

    void setCounter(int counter);


}
