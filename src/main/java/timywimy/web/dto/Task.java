package timywimy.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.tasks.converters.Priority;
import timywimy.web.dto.common.DateTimeZone;

import java.util.List;
import java.util.UUID;

@JsonPropertyOrder(value = {"id", "name", "description", "deadline", "parent", "children", "event", "priority", "completed"})
public class Task {

    private UUID id;
    private String name;
    private String description;

    private DateTimeZone deadline;

    private Task parent;
    private List<Task> children;
    private Event event;

    private Priority priority;
    private boolean completed;

    public Task() {
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

    public DateTimeZone getDeadline() {
        return deadline;
    }

    public void setDeadline(DateTimeZone deadline) {
        this.deadline = deadline;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
