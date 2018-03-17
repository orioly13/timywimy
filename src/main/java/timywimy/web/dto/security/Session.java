package timywimy.web.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import timywimy.web.util.converters.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonPropertyOrder(value = {"session", "expiry_ts", "user"})
public class Session {

    private UUID session;
    @JsonProperty("expiry_ts")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime expiryTs;
    private User user;

    public Session() {
    }

    public Session(UUID session, ZonedDateTime expiryTs, User user) {
        this.session = session;
        this.expiryTs = expiryTs;
        this.user = user;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    public ZonedDateTime getExpiryTs() {
        return expiryTs;
    }

    public void setExpiryTs(ZonedDateTime expiryTs) {
        this.expiryTs = expiryTs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
