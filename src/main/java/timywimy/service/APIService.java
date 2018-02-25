package timywimy.service;

import timywimy.web.dto.UserDTO;

import java.util.UUID;

public interface APIService {
    //registers user and returns session UUID
    UUID register(UserDTO userDTO);

    //opens new session
    UUID openSession(UserDTO userDTO);

    //closes current session
    boolean closeSession(UUID sessionId);

    //returs model.User by session
    timywimy.model.security.User getUserBySession(UUID sessionId);
}
