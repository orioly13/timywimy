package com.timywimy.model.bo.tasks;

import com.timywimy.model.bo.events.Event;
import com.timywimy.model.bo.tasks.converters.Priority;
import com.timywimy.model.bo.tasks.converters.PriorityConverter;
import com.timywimy.model.common.DefaultEntityImpl;
import com.timywimy.model.common.converters.DateTimeZone;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bo_tasks",
        indexes = {@Index(name = "bo_tasks_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
                @Index(name = "bo_tasks_idx_priority", columnList = "owner_id,priority")})
public class TaskImpl extends DefaultEntityImpl implements Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Task parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Task> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private TaskGroup group;

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "priority", columnDefinition = "numeric(1,0)")
    @Convert(converter = PriorityConverter.class)
    private Priority priority;
    @Column(name = "completed", columnDefinition = "boolean", nullable = false)
    private boolean completed;

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
    public Event getEvent() {
        return event;
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public TaskGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(TaskGroup group) {
        this.group = group;
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
