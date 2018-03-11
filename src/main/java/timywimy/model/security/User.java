package timywimy.model.security;

import timywimy.model.bo.events.Schedule;
import timywimy.model.bo.tasks.Task;
import timywimy.model.bo.tasks.TaskGroup;
import timywimy.model.common.AbstractNamedEntity;
import timywimy.model.security.converters.Role;
import timywimy.model.security.converters.RoleConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sec_users")
public class User extends AbstractNamedEntity {
    //unique corresponds to constraint
    @Column(name = "email", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String email;
    @Column(name = "password", columnDefinition = "varchar(50)", nullable = false)
    private String password;
    @Column(name = "role", columnDefinition = "numeric(2,0)")
    @Convert(converter = RoleConverter.class)
    private Role role;
    @Column(name = "active", columnDefinition = "boolean", nullable = false)
    private boolean active;
    @Column(name = "banned", columnDefinition = "boolean", nullable = false)
    private boolean banned;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_by")
    private User bannedBy;
    @Column(name = "banned_till", columnDefinition = "timestamp with time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime bannedTill;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Schedule> schedules;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<timywimy.model.bo.events.Event> events;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<TaskGroup> taskGroups;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Task> tasks;


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


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    public boolean isBanned() {
        return banned;
    }


    public void setBanned(boolean banned) {
        this.banned = banned;
    }


    public User getBannedBy() {
        return bannedBy;
    }


    public void setBannedBy(User bannedBy) {
        this.bannedBy = bannedBy;
    }


    public ZonedDateTime getBannedTill() {
        return bannedTill;
    }


    public void setBannedTill(ZonedDateTime bannedTill) {
        this.bannedTill = bannedTill;
    }


    public List<Schedule> getSchedules() {
        return schedules;
    }


    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }


    public List<timywimy.model.bo.events.Event> getEvents() {
        return events;
    }


    public void setEvents(List<timywimy.model.bo.events.Event> events) {
        this.events = events;
    }


    public List<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    public void setTaskGroups(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }


    public List<Task> getTasks() {
        return tasks;
    }


    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


}
