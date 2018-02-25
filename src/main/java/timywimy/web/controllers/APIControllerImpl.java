package timywimy.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import timywimy.service.APIService;
import timywimy.web.dto.UserDTO;

import java.util.UUID;

@Controller
public class APIControllerImpl implements APIController {

    private final APIService apiService;

    @Autowired
    public APIControllerImpl(APIService apiService) {
        Assert.notNull(apiService,"APIService should be provided");
        this.apiService = apiService;
    }

    @Override
    public UUID register(UserDTO user) {
        return apiService.register(user);
    }

    @Override
    public UUID openSession(UserDTO user) {
        return apiService.openSession(user);
    }

    @Override
    public boolean closeSession(UUID sessionId) {
        return apiService.closeSession(sessionId);
    }

//    public timywimy.model.security.User getUserBySession(UUID sessionId) {
//        return null;
//    }
}
