package timywimy.service.entities;


import timywimy.web.dto.tasks.Task;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskService extends EntityService<Task, timywimy.model.bo.tasks.Task> {

    List<Task> getBetween(UUID session, ZonedDateTime start, ZonedDateTime finish);

    List<Task> linkSubTasks(UUID parentTask, UUID session, List<Task> children);

    List<Task> unlinkSubTasks(UUID parentTask, UUID session, List<Task> children);

}
