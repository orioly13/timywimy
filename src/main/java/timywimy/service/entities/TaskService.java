package timywimy.service.entities;


import timywimy.web.dto.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService extends EntityService<Task, timywimy.model.bo.tasks.Task> {

    Task linkSubTasks(UUID parentTask, UUID session, List<Task> children);

    Task unlinkSubTasks(UUID parentTask, UUID session, List<Task> children);
//
//    boolean ban(UUID idToBan, UUID session, ZonedDateTime bannedTill);
//
//    boolean unBan(UUID bannedId, UUID session);

}
