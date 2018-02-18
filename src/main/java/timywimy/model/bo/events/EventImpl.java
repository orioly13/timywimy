package timywimy.model.bo.events;

import timywimy.model.bo.events.extensions.common.EventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.DefaultEntityImpl;
import timywimy.model.common.converters.DateTimeZone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bo_events", indexes = {
        @Index(name = "bo_events_idx_owner_id", columnList = "owner_id"),
        @Index(name = "bo_events_idx_date_time_zone", columnList = "owner_id,date,time,zone"),
        @Index(name = "bo_events_idx_name", columnList = "owner_id,name")})
public class EventImpl extends DefaultEntityImpl implements Event {

    @Embedded
    private DateTimeZone dateTimeZone;
    @Column(name = "duration", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime duration;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private List<EventExtension> extensions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private List<Task> tasks;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

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
