package timywimy.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import timywimy.service.APIService;
import timywimy.web.dto.InfoDTO;
import timywimy.web.dto.SessionDTO;
import timywimy.web.dto.UserDTO;
import timywimy.web.dto.common.Response;

import java.util.UUID;

@RestController //with RestController there is no need to add @ResponseBody to all requests
@RequestMapping("/")
public class RestAppControllerImpl implements RestAppController {
    //    private static final Logger log = LoggerFactory.getLogger(RestAppControllerImpl.class);
    private final APIService apiService;
    private final String apiName;
    private String apiVersion;

    @Autowired
    public RestAppControllerImpl(APIService apiService,
                                 @Value("${api.info.name}") String apiName,
                                 @Value("${api.info.version}") String apiVersion) {
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        Assert.notNull(apiService, "APIService should be provided");
        this.apiService = apiService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<InfoDTO> info(@RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        return new Response<>(requestId, new InfoDTO(apiName, apiVersion));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/register")
    public Response<SessionDTO> register(@RequestBody UserDTO user,
                                         @RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        UUID session = apiService.register(user);
        user.setPassword(null);
        return new Response<>(requestId, new SessionDTO(session, null, user));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/open-session")
    public Response<SessionDTO> openSession(@RequestBody UserDTO user,
                                            @RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        UUID session = apiService.openSession(user);
        user.setPassword(null);
        return new Response<>(requestId, new SessionDTO(session, null, user));

    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/close-session")
    public Response<Boolean> closeSession(@RequestHeader(name = "X-Auth-Session") UUID session,
                                          @RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        boolean res = apiService.closeSession(session);
        return new Response<>(requestId, res);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/update-profile")
    public Response<UserDTO> updateProfile(@RequestBody UserDTO user,
                                           @RequestHeader(name = "X-Auth-Session") UUID session,
                                           @RequestAttribute(value = "timywimy.request.id") Integer requestId) {
        UserDTO updatedUser = apiService.updateProfile(user, session);
        updatedUser.setPassword(null);
        updatedUser.setId(null);
        return new Response<>(requestId, updatedUser);
    }
}
