package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timywimy.repository.TaskRepository;
import timywimy.service.RestService;
import timywimy.web.dto.Task;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl extends AbstractOwnedEntityService<Task, timywimy.model.bo.tasks.Task>
        implements TaskService {

    @Autowired
    protected TaskServiceImpl(RestService restService, TaskRepository repository) {
        super(restService, repository);
    }

    @Override
    public Task get(UUID entityId, UUID userSession) {
        return null;
    }

    @Override
    public Task save(Task dto, UUID userSession) {
        return null;
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        return false;
    }

    @Override
    public List<Task> getAll(UUID userSession) {
        return null;
    }

    @Override
    public Task linkSubTasks(UUID parentTask, UUID session, List<Task> children) {
        return null;
    }

    @Override
    public Task unlinkSubTasks(UUID parentTask, UUID session, List<Task> children) {
        return null;
    }
}
