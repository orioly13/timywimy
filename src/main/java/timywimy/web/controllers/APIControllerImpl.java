package timywimy.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import timywimy.service.APIService;
import timywimy.util.RequestUtil;
import timywimy.web.dto.SessionDTO;
import timywimy.web.dto.UserDTO;
import timywimy.web.dto.InfoDTO;
import timywimy.web.dto.common.Response;

import java.util.UUID;

@Controller
@RequestMapping("/api")
public class APIControllerImpl implements APIController {
    private final APIService apiService;

    @Autowired
    public APIControllerImpl(APIService apiService) {
        Assert.notNull(apiService, "APIService should be provided");
        this.apiService = apiService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response<InfoDTO> info() {
        return new Response<>(RequestUtil.getRandomRequestId(), new InfoDTO());
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/register")
    public @ResponseBody Response<SessionDTO> register(@RequestBody UserDTO user) {
        UUID register = apiService.register(user);
        return null;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/open-session")
    public @ResponseBody Response<SessionDTO> openSession(@RequestBody UserDTO user) {
        UUID session = apiService.openSession(user);
        return null;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/close-session")
    public @ResponseBody Response<Boolean> closeSession(@RequestHeader(name = "X-Auth-Session") UUID session) {
        boolean b = apiService.closeSession(session);
        return null;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/update-profile")
    public Response<UserDTO> updateProfile(@RequestBody UserDTO user, @RequestHeader(name = "X-Auth-Session") UUID session) {
        //todo implement
        return new Response<>();
    }
}
