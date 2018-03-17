package timywimy.web.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.model.security.converters.Role;
import timywimy.web.dto.tasks.Task;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder(value = {"id", "email", "name", "password", "old_password",
        "token", "active", "banned", "banned_till","role","active","schedules","events","tasks"})
public class User {
    //todo expand with events and shit
    private UUID id;
    private String email;
    private String name;
    private String password;
    @JsonProperty("old_password")
    private String oldPassword;
    //todo create token
    private String token;
    private Boolean banned;
    @JsonProperty("banned_till")
    private ZonedDateTime bannedTill;
    private Role role;
    private Boolean active;

    private List<Schedule> schedules;
    private List<Event> events;
    private List<Task> tasks;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public ZonedDateTime getBannedTill() {
        return bannedTill;
    }

    public void setBannedTill(ZonedDateTime bannedTill) {
        this.bannedTill = bannedTill;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
