package timywimy.model.security;

import timywimy.model.bo.events.EventImpl;
import timywimy.model.bo.events.ScheduleImpl;
import timywimy.model.bo.tasks.TaskGroupImpl;
import timywimy.model.bo.tasks.TaskImpl;
import timywimy.model.common.AbstractNamedEntity;
import timywimy.model.common.NamedEntity;
import timywimy.model.security.converters.Role;
import timywimy.model.security.converters.RoleConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;

@Entity
@Table(name = "sec_users")
public class UserImpl extends AbstractNamedEntity implements NamedEntity {
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
    private UserImpl bannedBy;
    @Column(name = "banned_till", columnDefinition = "timestamp with time zone")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime bannedTill;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<ScheduleImpl> schedules;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<EventImpl> events;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<TaskGroupImpl> taskGroups;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<TaskImpl> tasks;


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


    public UserImpl getBannedBy() {
        return bannedBy;
    }


    public void setBannedBy(UserImpl bannedBy) {
        this.bannedBy = bannedBy;
    }


    public ZonedDateTime getBannedTill() {
        return bannedTill;
    }


    public void setBannedTill(ZonedDateTime bannedTill) {
        this.bannedTill = bannedTill;
    }


    public Collection<ScheduleImpl> getSchedules() {
        return schedules;
    }


    public void setSchedules(Collection<ScheduleImpl> schedules) {
        this.schedules = schedules;
    }


    public Collection<EventImpl> getEvents() {
        return events;
    }


    public void setEvents(Collection<EventImpl> events) {
        this.events = events;
    }


    public Collection<TaskGroupImpl> getTaskGroups() {
        return taskGroups;
    }

    public void setTaskGroups(Collection<TaskGroupImpl> taskGroups) {
        this.taskGroups = taskGroups;
    }


    public Collection<TaskImpl> getTasks() {
        return tasks;
    }


    public void setTasks(Collection<TaskImpl> tasks) {
        this.tasks = tasks;
    }


}
