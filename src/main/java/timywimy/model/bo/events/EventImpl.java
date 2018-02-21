package timywimy.model.bo.events;

import timywimy.model.bo.events.extensions.common.EventExtensionImpl;
import timywimy.model.bo.tasks.TaskImpl;
import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DateTimeZoneEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.converters.DateTimeZone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bo_events", indexes = {
        @Index(name = "bo_events_idx_owner_id", columnList = "owner_id"),
        @Index(name = "bo_events_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
        @Index(name = "bo_events_idx_name", columnList = "owner_id,name")})
public class EventImpl extends AbstractDefaultEntity implements DateTimeZoneEntity, DurableEntity {

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "duration", columnDefinition = "timestamp without time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime duration;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
//    private List<EventExtensionImpl> extensions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private List<TaskImpl> tasks;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private ScheduleImpl schedule;

//    public List<EventExtensionImpl> getExtensions() {
//        return extensions;
//    }
//
//    public void setExtensions(List<EventExtensionImpl> extensions) {
//        this.extensions = extensions;
//    }


    public List<TaskImpl> getTasks() {
        return tasks;
    }


    public void setTasks(List<TaskImpl> tasks) {
        this.tasks = tasks;
    }


    public ScheduleImpl getSchedule() {
        return schedule;
    }


    public void setSchedule(ScheduleImpl schedule) {
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
