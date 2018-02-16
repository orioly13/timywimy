package com.timywimy.model.bo.events;

import com.timywimy.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.bo.tasks.Task;
import com.timywimy.model.common.DefaultEntityImpl;
import com.timywimy.model.common.converters.DateTimeZone;
import com.timywimy.model.common.converters.PeriodDuration;
import com.timywimy.model.security.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "BO_EVENTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class EventImpl extends DefaultEntityImpl implements Event {

    //todo event templates
    private DateTimeZone dateTimeZone;
    private PeriodDuration duration;

    private List<EventExtension> extensions;
    private List<Task> tasks;
    private Schedule schedule;

    private User owner;

    @Override
    public List<EventExtension> getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(List<EventExtension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }

    @Override
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

    @Override
    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    @Override
    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    @Override
    public PeriodDuration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(PeriodDuration duration) {
        this.duration = duration;
    }
}
