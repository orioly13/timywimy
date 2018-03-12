package timywimy.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.model.security.converters.Role;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonPropertyOrder(value = {"id", "email", "name", "password", "old_password", "token", "banned", "banned_till"})
public class User {
    //todo expand with events and shit
    private UUID id;
    private String email;
    private String password;
    private Role role;
    private String name;
    private String token;
    @JsonProperty("old_password")
    private String oldPassword;
    private Boolean banned;
    @JsonProperty("banned_till")
    private ZonedDateTime bannedTill;

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
}
