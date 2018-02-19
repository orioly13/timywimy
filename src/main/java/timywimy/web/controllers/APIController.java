package timywimy.web.controllers;


import timywimy.web.dto.User;

import java.util.UUID;

public interface APIController {

    UUID register(User user);

    UUID openSession(User user);

    boolean closeSession(UUID sessionId);
}
