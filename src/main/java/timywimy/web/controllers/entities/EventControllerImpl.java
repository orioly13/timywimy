package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timywimy.service.entities.EventService;
import timywimy.util.TimeFormatUtil;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.tasks.Task;

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
                                    @RequestHeader(name = "X-Auth-Session") UUID session,
                                    @PathVariable("id") UUID entityId) {
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
                                            @RequestParam("start") String start,
                                            @RequestParam("fin") String finish) {

        return new Response<>(requestId, ((EventService) service).getBetween(session,
                TimeFormatUtil.parseZonedDateTime(start), TimeFormatUtil.parseZonedDateTime(finish)));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/link-tasks")
    public Response<List<Task>> linkTasks(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                          @RequestHeader(name = "X-Auth-Session") UUID session,
                                          @PathVariable("id") UUID event,
                                          @RequestBody List<Task> tasks) {
        return new Response<>(requestId, ((EventService) service).linkTasks(event, session, tasks));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/unlink-tasks")
    public Response<List<Task>> unlinkTasks(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                            @RequestHeader(name = "X-Auth-Session") UUID session,
                                            @PathVariable("id") UUID event,
                                            @RequestBody List<Task> tasks) {
        return new Response<>(requestId, ((EventService) service).unlinkTasks(event, session, tasks));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/add-extensions")
    public Response<Event> addExtensions(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                         @RequestHeader(name = "X-Auth-Session") UUID session,
                                         @PathVariable("id") UUID event,
                                         @RequestBody List<EventExtension> extensions) {
        return new Response<>(requestId, ((EventService) service).addExtensions(event, session, extensions));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/update-extensions")
    public Response<Event> updateExtensions(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                            @RequestHeader(name = "X-Auth-Session") UUID session,
                                            @PathVariable("id") UUID event,
                                            @RequestBody List<EventExtension> extensions) {
        return new Response<>(requestId, ((EventService) service).updateExtensions(event, session, extensions));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/delete-extensions")
    public Response<Event> deleteExtensions(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                            @RequestHeader(name = "X-Auth-Session") UUID session,
                                            @PathVariable("id") UUID event,
                                            @RequestBody List<EventExtension> extensions) {
        return new Response<>(requestId, ((EventService) service).deleteExtensions(event, session, extensions));
    }
}