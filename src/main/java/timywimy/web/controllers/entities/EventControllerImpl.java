package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timywimy.service.entities.EventService;
import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entities/events")
public class EventControllerImpl extends AbstractEntityController<Event, timywimy.model.bo.events.Event> implements EventController {

    @Autowired
    public EventControllerImpl(EventService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public Response<Event> get(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                               @RequestHeader(name = "X-Auth-Session") UUID session,
                               @PathVariable("id") UUID entityId) {
        return new Response<>(requestId, service.get(entityId, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    public Response<Event> create(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                  @RequestHeader(name = "X-Auth-Session") UUID session,
                                  @RequestBody Event entity) {
        entity.setId(null);
        return new Response<>(requestId, service.save(entity, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/update")
    public Response<Event> update(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                  @RequestHeader(name = "X-Auth-Session") UUID session,
                                  @PathVariable("id") UUID entityId,
                                  @RequestBody Event entity) {
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
    public Response<List<Event>> getAll(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                        @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, service.getAll(session));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/between")
    public Response<List<Event>> getBetween(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                            @RequestHeader(name = "X-Auth-Session") UUID session,
                                            @RequestParam("dateTimeZone") DateTimeZone start,
                                            @RequestParam("dateTimeZone") DateTimeZone finish) {
        return new Response<>(requestId, service.getAll(session));
    }
}