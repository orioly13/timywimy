package com.timywimy.model.security;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.bo.events.Schedule;
import com.timywimy.model.bo.tasks.Task;
import com.timywimy.model.bo.tasks.TaskGroup;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.security.converters.Role;

import java.time.ZonedDateTime;
import java.util.Collection;

public interface User extends NamedEntity {

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    Role getRole();

    void setRole(Role role);

    boolean isActive();

    void setActive(boolean active);

    boolean isBanned();

    void setBanned(boolean banned);

    User getBannedBy();

    void setBannedBy(User bannedBy);

    ZonedDateTime getBannedTill();

    void setBannedTill(ZonedDateTime bannedTill);

    //joined by hibernate
    Collection<Schedule> getSchedules();

    void setSchedules(Collection<Schedule> schedules);

    Collection<Event> getEvents();

    void setEvents(Collection<Event> events);

    Collection<TaskGroup> getTaskGroups();

    void setTaskGroups(Collection<TaskGroup> taskGroups);

    Collection<Task> getTasks();

    void setTasks(Collection<Task> tasks);


}
