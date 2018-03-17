package timywimy.service.entities;


import timywimy.model.common.util.DateTimeZone;
import timywimy.web.dto.tasks.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService extends EntityService<Task, timywimy.model.bo.tasks.Task> {

    List<Task> getBetween(UUID session, DateTimeZone start, DateTimeZone finish);

    List<Task> linkSubTasks(UUID parentTask, UUID session, List<Task> children);

    List<Task> unlinkSubTasks(UUID parentTask, UUID session, List<Task> children);

}
