package timywimy.web.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timywimy.service.entities.TaskService;
import timywimy.util.TimeFormatUtil;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.tasks.Task;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entities/tasks")
public class TaskControllerImpl extends AbstractEntityController<Task, timywimy.model.bo.tasks.Task> implements TaskController {

    @Autowired
    public TaskControllerImpl(TaskService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public Response<Task> get(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                              @RequestHeader(name = "X-Auth-Session") UUID session,
                              @PathVariable("id") UUID entityId) {
        return new Response<>(requestId, service.get(entityId, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    public Response<Task> create(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                 @RequestHeader(name = "X-Auth-Session") UUID session,
                                 @RequestBody Task entity) {
        entity.setId(null);
        return new Response<>(requestId, service.save(entity, session));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/update")
    public Response<Task> update(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                 @RequestHeader(name = "X-Auth-Session") UUID session,
                                 @PathVariable("id") UUID entityId,
                                 @RequestBody Task entity) {
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
    public Response<List<Task>> getAll(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                       @RequestHeader(name = "X-Auth-Session") UUID session) {
        return new Response<>(requestId, service.getAll(session));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/between")
    public Response<List<Task>> getBetween(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                           @RequestHeader(name = "X-Auth-Session") UUID session,
                                           @RequestParam("start") String start,
                                           @RequestParam("fin") String finish) {

        return new Response<>(requestId, ((TaskService) service).getBetween(session,
                TimeFormatUtil.parseZonedDateTime(start), TimeFormatUtil.parseZonedDateTime(finish)));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/link-tasks")
    public Response<List<Task>> linkChildren(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                             @RequestHeader(name = "X-Auth-Session") UUID session,
                                             @PathVariable("id") UUID parent,
                                             @RequestBody List<Task> tasks) {
        return new Response<>(requestId, ((TaskService) service).linkSubTasks(parent, session, tasks));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}/unlink-tasks")
    public Response<List<Task>> unlinkChildren(@RequestAttribute(value = "timywimy.request.id") Integer requestId,
                                               @RequestHeader(name = "X-Auth-Session") UUID session,
                                               @PathVariable("id") UUID parent,
                                               @RequestBody List<Task> tasks) {
        return new Response<>(requestId, ((TaskService) service).unlinkSubTasks(parent, session, tasks));
    }

}