package timywimy.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonPropertyOrder(value = {"session", "expiry_ts", "user"})
public class SessionDTO {

    private UUID session;
    @JsonProperty("expiry_ts")
    private ZonedDateTime expiryTs;
    private UserDTO user;

    public SessionDTO(UUID session, ZonedDateTime expiryTs, UserDTO user) {
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

}
