package timywimy.model.bo.tasks;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.tasks.converters.Priority;
import timywimy.model.bo.tasks.converters.PriorityConverter;
import timywimy.model.common.DateTimeZoneEntity;
import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.converters.DateTimeZone;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bo_tasks",
        indexes = {@Index(name = "bo_tasks_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
                @Index(name = "bo_tasks_idx_priority", columnList = "owner_id,priority")})
public class Task extends AbstractDefaultEntity implements DateTimeZoneEntity {

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

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    public boolean isCompleted() {
        return completed;
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public Event getEvent() {
        return event;
    }


    public void setEvent(Event event) {
        this.event = event;
    }


    public TaskGroup getGroup() {
        return group;
    }

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
