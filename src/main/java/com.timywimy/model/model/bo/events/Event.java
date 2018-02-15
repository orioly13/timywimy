package com.timywimy.model.model.bo.events;

import com.timywimy.model.model.bo.events.extensions.common.EventExtension;
import com.timywimy.model.model.bo.tasks.TaskImpl;
import com.timywimy.model.model.common.DurableEntityImpl;
import com.timywimy.util.IterableUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "BO_EVENTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class Event extends DurableEntityImpl {

    private List<EventExtension> extensions;

    private List<TaskImpl> tasks;

    private Schedule schedule;

    public Event() {
    }

    public List<EventExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<EventExtension> extensions) {
        this.extensions = extensions;
    }

    public List<TaskImpl> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskImpl> tasks) {
        this.tasks = tasks;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    //other methods

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (extensions != null) {
            extensions.forEach(eventExtension ->
                    sb.append(eventExtension).append(";"));
        }
        return String.format("Event(%s, extensions:[%s])", super.toString(), sb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Event)) {
            return false;
        }
        Event that = (Event) o;
        return IterableUtil.equals(extensions, that.extensions) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extensions, super.hashCode());
    }
}
