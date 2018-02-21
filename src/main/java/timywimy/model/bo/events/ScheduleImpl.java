package timywimy.model.bo.events;

import timywimy.model.common.DefaultEntityImpl;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bo_schedules",
        indexes = {@Index(name = "bo_schedules_idx_owner_id", columnList = "owner_id"),
                @Index(name = "bo_schedules_idx_name", columnList = "owner_id,name")})
public class ScheduleImpl extends DefaultEntityImpl implements Schedule {

    @Column(name = "cron", columnDefinition = "varchar(20)")
    private String cron;
    @Column(name = "duration", columnDefinition = "timestamp without time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime duration;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    private List<Event> instances;

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public List<Event> getInstances() {
        return instances;
    }

    @Override
    public void setInstances(List<Event> instances) {
        this.instances = instances;
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
