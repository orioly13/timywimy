package timywimy.web.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.tasks.Task;
import timywimy.web.util.converters.DateTimeZoneSerializer;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder(value = {"id", "name", "description", "event_ts", "duration", "extensions", "schedule", "tasks"})
public class Event {

    private UUID id;
    private String name;
    private String description;

    @JsonProperty("event_ts")
    @JsonSerialize(using = DateTimeZoneSerializer.class)
    private DateTimeZone dateTimeZone;
    private Duration duration;
    @JsonProperty("extensions")
    private List<EventExtension> eventExtensions;

    private Schedule schedule;
    private List<Task> tasks;


    public Event() {
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

    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<EventExtension> getEventExtensions() {
        return eventExtensions;
    }

    public void setEventExtensions(List<EventExtension> eventExtensions) {
        this.eventExtensions = eventExtensions;
    }
}
