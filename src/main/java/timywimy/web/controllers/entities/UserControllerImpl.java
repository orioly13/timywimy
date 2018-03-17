package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timywimy.service.entities.UserService;
import timywimy.web.dto.security.User;
import timywimy.web.dto.common.Response;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entities/users")
public class UserControllerImpl extends AbstractEntityController<User, timywimy.model.security.User> implements UserController {

    @Autowired
    public UserControllerImpl(UserService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public Response<User> get(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                              @RequestHeader(name = "X-Auth-Session") UUID session,
                              @PathVariable("id") UUID entityId) {
        return new Response<>(requestId, service.get(entityId, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    public Response<User> create(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                 @RequestHeader(name = "X-Auth-Session") UUID session,
                                 @RequestBody User entity) {
        entity.setId(null);
        return new Response<>(requestId, service.save(entity, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/update")
    public Response<User> update(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                 @RequestHeader(name = "X-Auth-Session") UUID session,
                                 @PathVariable("id") UUID entityId,
                                 @RequestBody User entity) {
        entity.setId(entityId);
        return new Response<>(requestId, service.save(entity, session));
    }


    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/delete")
    public Response<Boolean> delete(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                    @PathVariable("id") UUID entityId,
                                    @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, service.delete(entityId, session));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<User>> getAll(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                       @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, service.getAll(session));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/byEmail")
    public Response<User> getByEmail(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                     @RequestParam("email") String email,
                                     @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, ((UserService) service).getByEmail(email, session));
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/ban")
    public Response<Boolean> ban(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                 @PathVariable("id") UUID idToBan,
                                 @RequestHeader(name = "X-Auth-Session") UUID bannedBy,
                                 @RequestParam(value = "banned_till", required = false) ZonedDateTime bannedTill) {
        return new Response<>(requestId, ((UserService) service).ban(idToBan, bannedBy, bannedTill));
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/unban")
    public Response<Boolean> unBan(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                   @PathVariable("id") UUID bannedId,
                                   @RequestHeader(name = "X-Auth-Session") UUID unbannedBy) {
        return new Response<>(requestId, ((UserService) service).unBan(bannedId, unbannedBy));
    }

}