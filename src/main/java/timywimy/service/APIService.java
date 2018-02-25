package timywimy.service;

import timywimy.model.security.User;
import timywimy.web.dto.UserDTO;

import java.util.UUID;

public interface APIService {
    //registers user and returns session UUID
    UUID register(UserDTO userDTO);

    //opens new session
    UUID openSession(UserDTO userDTO);

    //closes current session
    boolean closeSession(UUID sessionId);

    //closes current session
    UserDTO updateProfile(UserDTO user, UUID sessionId);

    //returs model.User by session
    User getUserBySession(UUID sessionId);
}
