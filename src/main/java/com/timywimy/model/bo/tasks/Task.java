package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.DateTimeZoneEntity;

import java.util.Collection;


public interface Task extends DateTimeZoneEntity {

    Collection<Task> getParents();

    void setParents(Collection<Task> parents);

    Collection<Task> getChildren();

    void setChildren(Collection<Task> children);

    boolean isDone();

    void setDone(boolean done);

    Event getEvent();

    void setEvent(Event event);


}
