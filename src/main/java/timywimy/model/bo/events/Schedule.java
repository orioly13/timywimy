package timywimy.model.bo.events;

import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.converters.DurationConverter;

import javax.persistence.*;
import java.time.Duration;
import java.util.Collection;

@Entity
@Table(name = "bo_schedules",
        indexes = {@Index(name = "bo_schedules_idx_owner_id", columnList = "owner_id"),
                @Index(name = "bo_schedules_idx_name", columnList = "owner_id,name")})
public class Schedule extends AbstractDefaultEntity implements DurableEntity {

    @Column(name = "cron", columnDefinition = "varchar(20)")
    private String cron;
    @Column(name = "duration", columnDefinition = "varchar(20)")
    @Convert(converter = DurationConverter.class)
    private Duration duration;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    private Collection<Event> instances;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Collection<Event> getInstances() {
        return instances;
    }

    public void setInstances(Collection<Event> instances) {
        this.instances = instances;
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
