package timywimy.model.bo.tasks;

import timywimy.model.bo.events.EventImpl;
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
public class TaskImpl extends AbstractDefaultEntity implements DateTimeZoneEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskImpl parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<TaskImpl> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventImpl event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private TaskGroupImpl group;

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "priority", columnDefinition = "numeric(1,0)")
    @Convert(converter = PriorityConverter.class)
    private Priority priority;
    @Column(name = "completed", columnDefinition = "boolean", nullable = false)
    private boolean completed;

    public TaskImpl getParent() {
        return parent;
    }

    public void setParent(TaskImpl parent) {
        this.parent = parent;
    }

    public List<TaskImpl> getChildren() {
        return children;
    }

    public void setChildren(List<TaskImpl> children) {
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


    public EventImpl getEvent() {
        return event;
    }


    public void setEvent(EventImpl event) {
        this.event = event;
    }


    public TaskGroupImpl getGroup() {
        return group;
    }

    public void setGroup(TaskGroupImpl group) {
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
