package timywimy.model.bo.events;

import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DateTimeZoneEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.DateTimeZone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "bo_events", indexes = {
        @Index(name = "bo_events_idx_owner_id", columnList = "owner_id"),
        @Index(name = "bo_events_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
        @Index(name = "bo_events_idx_name", columnList = "owner_id,name")})
public class Event extends AbstractDefaultEntity implements DateTimeZoneEntity, DurableEntity {

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "duration", columnDefinition = "timestamp without time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime duration;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event", targetEntity = AbstractEventExtension.class)
    private Collection<AbstractEventExtension> extensions;
    //THIS OT @LazyCollection(LazyCollectionOption.FALSE) (two eager collections cause excetions)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event",targetEntity = Task.class)
    private Collection<Task> tasks;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Collection<AbstractEventExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(Collection<AbstractEventExtension> extensions) {
        this.extensions = extensions;
    }


    public Collection<Task> getTasks() {
        return tasks;
    }


    public void setTasks(Collection<Task> tasks) {
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
    public LocalDateTime getDuration() {
        return duration;
    }

    @Override
    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }
}
