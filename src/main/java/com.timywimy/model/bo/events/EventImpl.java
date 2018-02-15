package com.timywimy.model.bo.events;

import com.timywimy.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.bo.tasks.Task;
import com.timywimy.model.common.DurableEntityImpl;
import com.timywimy.model.security.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "BO_EVENTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class EventImpl extends DurableEntityImpl implements Event {

    //todo event templates

    private List<EventExtension> extensions;

    private List<Task> tasks;

    private Schedule schedule;

    private User owner;

    public List<EventExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<EventExtension> extensions) {
        this.extensions = extensions;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
