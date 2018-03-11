package timywimy.model.bo.events;

import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DateTimeZoneEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.converters.DurationConverter;
import timywimy.model.common.util.DateTimeZone;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "bo_events", indexes = {
        @Index(name = "bo_events_idx_owner_id", columnList = "owner_id"),
        @Index(name = "bo_events_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
        @Index(name = "bo_events_idx_name", columnList = "owner_id,name")})
public class Event extends AbstractDefaultEntity implements DateTimeZoneEntity, DurableEntity {

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "duration", columnDefinition = "varchar(12)")
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event",
            targetEntity = AbstractEventExtension.class,
            orphanRemoval = true, cascade = CascadeType.PERSIST)
    //todo undertand WHY orphanRemoval NEEDS Cascade.PERSIST
    @OrderBy("order ASC")
    private List<AbstractEventExtension> extensions;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event",
            targetEntity = Task.class)
    @OrderBy("dateTimeZone ASC")
    private List<Task> tasks;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public List<AbstractEventExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<AbstractEventExtension> extensions) {
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
    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    @Override
    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
