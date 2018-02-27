package timywimy.web.controllers;


import timywimy.web.dto.SessionDTO;
import timywimy.web.dto.UserDTO;
import timywimy.web.dto.InfoDTO;
import timywimy.web.dto.common.Response;

import java.util.UUID;

public interface RestAppController {

    Response<InfoDTO> info(Integer requestId);

    Response<SessionDTO> register(UserDTO user, Integer requestId);

    Response<SessionDTO> openSession(UserDTO user, Integer requestId);

    Response<Boolean> closeSession(UUID session, Integer requestId);

    Response<UserDTO> updateProfile(UserDTO user, UUID session, Integer requestId);
}
