package timywimy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import timywimy.service.APIService;
import timywimy.util.RequestUtil;
import timywimy.util.exception.APIException;
import timywimy.web.dto.InfoDTO;
import timywimy.web.dto.SessionDTO;
import timywimy.web.dto.UserDTO;
import timywimy.web.dto.common.Response;

import java.util.UUID;

@RestController //with RestController there is no need to add @ResponseBody to all requests
@RequestMapping("/")
public class APIControllerImpl implements APIController {
    private static final Logger log = LoggerFactory.getLogger(APIControllerImpl.class);
    private final APIService apiService;
    private final String apiName;
    private String apiVersion;

    @Autowired
    public APIControllerImpl(APIService apiService, @Value("${api.info.name}") String apiName, @Value("${api.info.version}") String apiVersion) {
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        Assert.notNull(apiService, "APIService should be provided");
        this.apiService = apiService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<InfoDTO> info() {
        int requestId = RequestUtil.getRandomRequestId();
        log.info("[{}] GET to url:{}", requestId, "\"/timywimy/\"");
        return new Response<>(requestId, new InfoDTO(apiName, apiVersion));
    }

    //todo simplify
    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/register")
    public Response<SessionDTO> register(@RequestBody UserDTO user) {
        int requestId = RequestUtil.getRandomRequestId();
        log.info("[{}] POST to url:{}", requestId, "\"/timywimy/api/register\"");
        try {
            UUID session = apiService.register(user);
            user.setPassword(null);
            return new Response<>(requestId, new SessionDTO(session, null, user));
        } catch (APIException e) {
            log.error(String.format("[%s] code:%s", requestId, e.getErrorCode()), e);
            return new Response<>(requestId, e.getErrorCode());
        }
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/open-session")
    public Response<SessionDTO> openSession(@RequestBody UserDTO user) {
        int requestId = RequestUtil.getRandomRequestId();
        log.info("[{}] POST to url:{}", requestId, "\"/timywimy/api/open-session\"");
        try {
            UUID session = apiService.openSession(user);
            user.setPassword(null);
            return new Response<>(requestId, new SessionDTO(session, null, user));
        } catch (APIException e) {
            log.error(String.format("[%s] code:%s", requestId, e.getErrorCode()), e);
            return new Response<>(requestId, e.getErrorCode());
        }
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/close-session")
    public Response<Boolean> closeSession(@RequestHeader(name = "X-Auth-Session") UUID session) {
        int requestId = RequestUtil.getRandomRequestId();
        log.info("[{}] POST to url:{}", requestId, "\"/timywimy/api/close-session\"");
        try {
            boolean res = apiService.closeSession(session);
            return new Response<>(requestId, res);
        } catch (APIException e) {
            log.error(String.format("[%s] code:%s", requestId, e.getErrorCode()), e);
            return new Response<>(requestId, e.getErrorCode());
        }
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/update-profile")
    public Response<UserDTO> updateProfile(@RequestBody UserDTO user, @RequestHeader(name = "X-Auth-Session") UUID session) {
        int requestId = RequestUtil.getRandomRequestId();
        log.info("[{}] POST to url:{}", requestId, "\"/timywimy/api/update-profile\"");
        try {
            UserDTO updatedUser = apiService.updateProfile(user, session);
            updatedUser.setPassword(null);
            updatedUser.setId(null);
            return new Response<>(requestId, updatedUser);
        } catch (APIException e) {
            log.error(String.format("[%s] code:%s", requestId, e.getErrorCode()), e);
            return new Response<>(requestId, e.getErrorCode());
        }
    }
}
