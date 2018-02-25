package timywimy.service;

import timywimy.model.security.User;
import timywimy.web.dto.UserDTO;

import java.util.UUID;

public interface ProfileService {

    User get(UUID entityId, UUID userSession);

    User update(UserDTO user, UUID userSession);
}
