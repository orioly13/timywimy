package timywimy.web.controllers;


import timywimy.web.dto.SessionDTO;
import timywimy.web.dto.UserDTO;
import timywimy.web.dto.InfoDTO;
import timywimy.web.dto.common.Response;

import java.util.UUID;

public interface APIController {

    Response<InfoDTO> info();

    Response<SessionDTO> register(UserDTO user);

    Response<SessionDTO> openSession(UserDTO user);

    Response<Boolean> closeSession(UUID session);

    Response<UserDTO> updateProfile(UserDTO user, UUID session);
}
