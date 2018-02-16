package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.bo.tasks.converters.Priority;
import com.timywimy.model.common.BaseEntityImpl;
import com.timywimy.model.common.converters.DateTimeZone;
import com.timywimy.model.security.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class TaskImpl extends BaseEntityImpl implements Task {

    private Task parent;
    private List<Task> children;

    private Event event;
    private User owner;

    private DateTimeZone dateTimeZone;
    private Priority priority;
    private boolean completed;
    private String description;

    @Override
    public Task getParent() {
        return parent;
    }

    @Override
    public void setParent(Task parent) {
        this.parent = parent;
    }

    @Override
    public List<Task> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Task> children) {
        this.children = children;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
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
}
