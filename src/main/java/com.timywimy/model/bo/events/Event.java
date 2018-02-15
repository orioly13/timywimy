package com.timywimy.model.bo.events;

import com.timywimy.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.bo.tasks.Task;
import com.timywimy.model.common.DurableEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;


public interface Event extends DurableEntity, OwnedEntity {

    List<EventExtension> getExtensions();

    void setExtensions(List<EventExtension> extensions);

    List<Task> getTasks();

    void setTasks(List<Task> tasks);

    Schedule getSchedule();

    void setSchedule(Schedule schedule);
}
