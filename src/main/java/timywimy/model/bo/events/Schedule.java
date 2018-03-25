package timywimy.model.bo.events;

import timywimy.model.common.AbstractDefaultEntity;
import timywimy.model.common.DurableEntity;
import timywimy.model.common.converters.DurationConverter;
import timywimy.model.common.converters.ZoneIdConverter;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;

@Entity
@Table(name = "bo_schedules",
        indexes = {@Index(name = "bo_schedules_idx_owner_id", columnList = "owner_id"),
                @Index(name = "bo_schedules_idx_name", columnList = "owner_id,name")})
public class Schedule extends AbstractDefaultEntity implements DurableEntity {

    @Column(name = "cron", columnDefinition = "varchar(20)")
    private String cron;
    @Column(name = "zone", columnDefinition = "varchar(20)")
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zone;
    @Column(name = "duration", columnDefinition = "varchar(12)")
    @Convert(converter = DurationConverter.class)
    private Duration duration;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    @OrderBy("dateTimeZone ASC")
    private List<Event> instances;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public List<Event> getInstances() {
        return instances;
    }

    public void setInstances(List<Event> instances) {
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

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }
}
