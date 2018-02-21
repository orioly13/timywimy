package timywimy.model.bo.events;

import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DurableEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bo_schedules",
        indexes = {@Index(name = "bo_schedules_idx_owner_id", columnList = "owner_id"),
                @Index(name = "bo_schedules_idx_name", columnList = "owner_id,name")})
public class ScheduleImpl extends AbstractDefaultEntity implements DurableEntity {

    @Column(name = "cron", columnDefinition = "varchar(20)")
    private String cron;
    @Column(name = "duration", columnDefinition = "timestamp without time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime duration;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    private List<EventImpl> instances;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public List<EventImpl> getInstances() {
        return instances;
    }

    public void setInstances(List<EventImpl> instances) {
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
