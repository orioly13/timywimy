package timywimy.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import timywimy.service.APIService;
import timywimy.web.dto.User;

import java.util.UUID;

@Controller
public class APIControllerImpl implements APIController {

    private final APIService apiService;

    @Autowired
    public APIControllerImpl(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public UUID register(User user) {
        return apiService.register(user);
    }

    @Override
    public UUID openSession(User user) {
        return apiService.openSession(user);
    }

    @Override
    public boolean closeSession(UUID sessionId) {
        return apiService.closeSession(sessionId);
    }

    public timywimy.model.security.User getUserBySession(UUID sessionId) {
        return null;
    }
}
