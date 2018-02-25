package timywimy.web.dto;

import timywimy.web.dto.common.BasicDTO;

import java.time.ZonedDateTime;
import java.util.UUID;

public class SessionDTO implements BasicDTO {

    private UUID session;
    //todo property name and convert
    private ZonedDateTime expiryTs;
    private UserDTO user;

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
