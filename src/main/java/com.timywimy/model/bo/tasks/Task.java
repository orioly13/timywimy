package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.bo.tasks.converters.Priority;
import com.timywimy.model.common.DateTimeZoneEntity;
import com.timywimy.model.common.DescribedEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;


public interface Task extends OwnedEntity, DescribedEntity, DateTimeZoneEntity {

    Task getParent();

    void setParent(Task parent);

    List<Task> getChildren();

    void setChildren(List<Task> children);

    Priority getPriority();

    void setPriority(Priority priority);

    boolean isCompleted();

    void setCompleted(boolean completed);

    Event getEvent();

    void setEvent(Event event);
}
