package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timywimy.service.entities.ScheduleService;
import timywimy.util.TimeFormatUtil;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entities/schedules")
public class ScheduleControllerImpl extends AbstractEntityController<Schedule, timywimy.model.bo.events.Schedule> implements ScheduleController {

    @Autowired
    public ScheduleControllerImpl(ScheduleService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public Response<Schedule> get(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                  @RequestHeader(name = "X-Auth-Session") UUID session,
                                  @PathVariable("id") UUID entityId) {
        return new Response<>(requestId, service.get(entityId, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    public Response<Schedule> create(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                     @RequestHeader(name = "X-Auth-Session") UUID session,
                                     @RequestBody Schedule entity) {
        entity.setId(null);
        return new Response<>(requestId, service.save(entity, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/update")
    public Response<Schedule> update(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                     @RequestHeader(name = "X-Auth-Session") UUID session,
                                     @PathVariable("id") UUID entityId,
                                     @RequestBody Schedule entity) {
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
    public Response<List<Schedule>> getAll(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                           @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, service.getAll(session));
    }


    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/add-instances")
    public Response<List<Event>> addInstances(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                              @RequestHeader(name = "X-Auth-Session") UUID session,
                                              @PathVariable("id") UUID schedule,
                                              @RequestBody List<Event> instnaces) {
        return new Response<>(requestId, ((ScheduleService) service).addInstances(schedule, session, instnaces));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/delete-instances")
    public Response<List<Event>> deleteInstances(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                                 @RequestHeader(name = "X-Auth-Session") UUID session,
                                                 @PathVariable("id") UUID schedule,
                                                 @RequestBody List<Event> instances) {
        return new Response<>(requestId, ((ScheduleService) service).deleteInstances(schedule, session, instances));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/next-occurrences")
    public Response<List<ZonedDateTime>> getNextOccurrences(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                                            @RequestHeader(name = "X-Auth-Session") UUID session,
                                                            @PathVariable("id") UUID schedule,
                                                            @RequestParam("start") String start,
                                                            @RequestParam(value = "days", required = false) Integer days,
                                                            @RequestParam(value = "max", required = false) Integer max) {
        return new Response<>(requestId, ((ScheduleService) service).getNextOccurrences(
                schedule, session, TimeFormatUtil.parseZonedDateTime(start), days, max));
    }
}