package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.common.DateTimeZoneEntityImpl;
import com.timywimy.model.security.User;

import java.util.List;

public class TaskImpl extends DateTimeZoneEntityImpl implements Task {
    private Task parent;
    private List<Task> children;
    private Priority priority;
    private boolean done;

    private Event event;
    private User owner;

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
    public boolean isDone() {
        return done;
    }

    @Override
    public void setDone(boolean done) {
        this.done = done;
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
}
