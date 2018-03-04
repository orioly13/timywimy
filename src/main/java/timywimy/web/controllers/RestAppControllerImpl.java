package timywimy.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import timywimy.service.RestService;
import timywimy.web.dto.Info;
import timywimy.web.dto.Session;
import timywimy.web.dto.User;
import timywimy.web.dto.common.Response;

import java.util.UUID;

@RestController //with RestController there is no need to add @ResponseBody to all requests
@RequestMapping("/")
public class RestAppControllerImpl implements RestAppController {
    private final RestService restService;
    private final String apiName;
    private final String apiVersion;

    @Autowired
    public RestAppControllerImpl(RestService restService,
                                 @Value("${api.info.name}") String apiName,
                                 @Value("${api.info.version}") String apiVersion) {
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        Assert.notNull(restService, "APIService should be provided");
        this.restService = restService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Info> info(@RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        return new Response<>(requestId, new Info(apiName, apiVersion));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/register")
    public Response<Session> register(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                      @RequestBody User user) {
        Session session = restService.register(user);
        return new Response<>(requestId, session);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/open-session")
    public Response<Session> openSession(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                         @RequestBody User user) {
        Session session = restService.openSession(user);
        return new Response<>(requestId, session);

    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/close-session")
    public Response<Boolean> closeSession(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                          @RequestHeader(name = "X-Auth-Session") UUID session) {
        boolean res = restService.closeSession(session);
        return new Response<>(requestId, res);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/update-profile")
    public Response<User> updateProfile(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                        @RequestHeader(name = "X-Auth-Session") UUID session,
                                        @RequestBody User user) {
        User updatedUser = restService.updateProfile(session, user);
        return new Response<>(requestId, updatedUser);
    }
}
