package timywimy.web.controllers.entities;


import timywimy.web.dto.common.Response;
import timywimy.web.dto.tasks.Task;

import java.util.List;
import java.util.UUID;

public interface TaskController extends EntityController<Task> {

    Response<List<Task>> getBetween(Integer requestId, UUID session, String start, String finish);

    Response<List<Task>> linkChildren(Integer requestId, UUID session, UUID parent, List<Task> tasks);

    Response<List<Task>> unlinkChildren(Integer requestId, UUID session, UUID parent, List<Task> tasks);
}
