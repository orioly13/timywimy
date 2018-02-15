package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.DateTimeZoneEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;


public interface Task extends DateTimeZoneEntity, OwnedEntity {

    Task getParent();

    void setParent(Task parent);

    List<Task> getChildren();

    void setChildren(List<Task> children);

    Priority getPriority();

    void setPriority(Priority priority);

    boolean isDone();

    void setDone(boolean done);

    Event getEvent();

    void setEvent(Event event);
}
