package timywimy.model.security;

import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.Schedule;
import timywimy.model.bo.tasks.Task;
import timywimy.model.bo.tasks.TaskGroup;
import timywimy.model.common.NamedEntityImpl;
import timywimy.model.security.converters.Role;
import timywimy.model.security.converters.RoleConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;

@Entity
@Table(name = "sec_users")
public class UserImpl extends NamedEntityImpl implements User {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_by")
    private User bannedBy;
    @Column(name = "banned_till", columnDefinition = "timestamp with time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime bannedTill;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<Schedule> schedules;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<Event> events;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<TaskGroup> taskGroups;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<Task> tasks;


    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isBanned() {
        return banned;
    }

    @Override
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public User getBannedBy() {
        return bannedBy;
    }

    @Override
    public void setBannedBy(User bannedBy) {
        this.bannedBy = bannedBy;
    }

    @Override
    public ZonedDateTime getBannedTill() {
        return bannedTill;
    }

    @Override
    public void setBannedTill(ZonedDateTime bannedTill) {
        this.bannedTill = bannedTill;
    }

    @Override
    public Collection<Schedule> getSchedules() {
        return schedules;
    }

    @Override
    public void setSchedules(Collection<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public Collection<Event> getEvents() {
        return events;
    }

    @Override
    public void setEvents(Collection<Event> events) {
        this.events = events;
    }

    @Override
    public Collection<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    @Override
    public void setTaskGroups(Collection<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }


}
