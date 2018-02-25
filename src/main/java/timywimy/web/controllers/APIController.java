package timywimy.web.controllers;


import timywimy.web.dto.UserDTO;

import java.util.UUID;

public interface APIController {

    UUID register(UserDTO user);

    UUID openSession(UserDTO user);

    boolean closeSession(UUID sessionId);
}
