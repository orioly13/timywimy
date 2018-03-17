package timywimy.web.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.model.bo.events.Event;
import timywimy.web.dto.common.DateTimeZone;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder(value = {"id", "name", "description", "schedule_ts", "duration", "cron", "instances"})
public class Schedule {

    private UUID id;
    private String name;
    private String description;

    private Duration duration;
    private String cron;

    private List<Event> instances;


    public Schedule() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

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
}
