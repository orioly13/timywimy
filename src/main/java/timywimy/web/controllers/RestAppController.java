package timywimy.web.controllers;


import timywimy.web.dto.Info;
import timywimy.web.dto.security.Session;
import timywimy.web.dto.security.User;
import timywimy.web.dto.common.Response;

import java.util.UUID;

public interface RestAppController {

    Response<Info> info(Integer requestId);

    Response<Session> register(Integer requestId, User user);

    Response<Session> openSession(Integer requestId, User user);

    Response<Boolean> closeSession(Integer requestId, UUID session);

    Response<User> updateProfile(Integer requestId, UUID session, User user);
}
