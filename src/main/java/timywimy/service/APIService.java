package timywimy.service;

import timywimy.web.dto.User;

import java.util.UUID;

public interface APIService {
    //registers user and returns session UUID
    UUID register(User userDTO);

    //opens new session
    UUID openSession(User userDTO);

    //closes current session
    boolean closeSession(UUID sessionId);

    //returs model.User by session
    timywimy.model.security.User getUserBySession(UUID sessionId);
}
