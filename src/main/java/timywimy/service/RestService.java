package timywimy.service;

import timywimy.web.dto.security.Session;
import timywimy.web.dto.security.User;

import java.util.UUID;

public interface RestService {
    //registers user and returns session UUID
    Session register(User user);

    //opens new session
    Session openSession(User user);

    //closes current session
    boolean closeSession(UUID sessionId);

    //closes current session
    User updateProfile(UUID sessionId, User user);

    //returs model.User by session
    timywimy.model.security.User getUserBySession(UUID sessionId);
}
