package timywimy.service.dto;

import timywimy.model.security.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public class UserSession {

    private UUID uuid;
    private User user;
    private ZonedDateTime expiryDate;

    public UserSession(User user) {
        this.user = user;
        this.uuid = UUID.randomUUID();
        this.expiryDate = ZonedDateTime.now().plusMinutes(30L);
    }

    public UserSession(UUID session, User user) {
        this.user = user;
        this.uuid = session;
        this.expiryDate = ZonedDateTime.now().plusMinutes(30L);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(ZonedDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
