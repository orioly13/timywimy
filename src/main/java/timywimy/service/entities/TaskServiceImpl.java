package timywimy.service.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timywimy.model.common.util.DateTimeZone;
import timywimy.model.security.User;
import timywimy.repository.TaskRepository;
import timywimy.service.RestService;
import timywimy.service.converters.Converter;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.dto.Task;

import java.util.ArrayList;
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
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, entityId, "entityId");


        timywimy.model.bo.tasks.Task task = repository.get(entityId);
        if (task == null) {
            return null;
        }
        assertOwner(task, userBySession);

        return Converter.taskEntityToTaskDTO(task);
    }

    @Override
    public Task save(Task dto, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, dto, "task");

        timywimy.model.bo.tasks.Task task = repository.get(dto.getId());
        if (task == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "task not found");
        }
        assertOwner(task, userBySession);

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setCompleted(dto.isCompleted());
        task.setDateTimeZone(Converter.dateTimeZoneDTOToEntity(dto.getDeadline()));

        return Converter.taskEntityToTaskDTO(repository.save(task, userSession));
    }

    @Override
    public boolean delete(UUID entityId, UUID userSession) {
        User userBySession = getUserBySession(userSession);
        RequestUtil.validateEmptyField(ServiceException.class, entityId, "entityId");

        timywimy.model.bo.tasks.Task task = repository.get(entityId);
        if (task == null) {
            throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "task not found");
        }
        assertOwner(task, userBySession);

        return repository.delete(task);
    }

    @Override
    public List<Task> getAll(UUID userSession) {
        User userBySession = getUserBySession(userSession);

        List<timywimy.model.bo.tasks.Task> allByOwner = ((TaskRepository) repository).getAllByOwner(userBySession.getId());

        List<Task> result = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task ownedTask : allByOwner) {
            result.add(Converter.taskEntityToTaskDTO(ownedTask));
        }
        return result;
    }

    @Override
    public List<Task> getBetween(UUID session, DateTimeZone start, DateTimeZone finish) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, session, "session");
        RequestUtil.validateEmptyField(ServiceException.class, start, "start");
        RequestUtil.validateEmptyField(ServiceException.class, finish, "finish");
        if (start.isAfter(finish)) {
            throw new ServiceException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "start is after end");
        }


        List<timywimy.model.bo.tasks.Task> byOwnerBetween = ((TaskRepository) repository).getByOwnerBetween(userBySession.getId(), start, finish);

        List<Task> res = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task task : byOwnerBetween) {
            res.add(Converter.taskEntityToTaskDTO(task));
        }
        return res;
    }

    @Override
    public List<Task> linkSubTasks(UUID parentTask, UUID session, List<Task> children) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, parentTask, "parent task");
        for (Task child : children) {
            RequestUtil.validateEmptyField(ServiceException.class, child, "child task");
            RequestUtil.validateEmptyField(ServiceException.class, child.getId(), "child task id");
        }

        List<timywimy.model.bo.tasks.Task> tasksToLink = new ArrayList<>();
        for (Task child : children) {
            timywimy.model.bo.tasks.Task loadedChild = repository.get(child.getId());
            if (loadedChild == null) {
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of children not found");
            }
            tasksToLink.add(loadedChild);
        }

        List<timywimy.model.bo.tasks.Task> tasks = ((TaskRepository) repository).
                linkChildren(parentTask, tasksToLink, userBySession.getId());

        List<Task> res = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task task : tasks) {
            res.add(Converter.taskEntityToTaskDTO(task));
        }

        return res;
    }

    @Override
    public List<Task> unlinkSubTasks(UUID parentTask, UUID session, List<Task> children) {
        User userBySession = getUserBySession(session);
        RequestUtil.validateEmptyField(ServiceException.class, parentTask, "parent task");
        for (Task child : children) {
            RequestUtil.validateEmptyField(ServiceException.class, child, "child task");
            RequestUtil.validateEmptyField(ServiceException.class, child.getId(), "child task id");
        }

        List<timywimy.model.bo.tasks.Task> tasks = ((TaskRepository) repository).getAllByOwner(userBySession.getId());
        List<timywimy.model.bo.tasks.Task> tasksToUnlink=new ArrayList<>();
        for (Task child : children) {
            boolean foundToUnlink=false;
            for(timywimy.model.bo.tasks.Task task:tasks){
                if(task.getId().equals(child.getId())){
                    foundToUnlink=true;
                    tasksToUnlink.add(task);
                    break;
                }
            }
            if(!foundToUnlink){
                throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND, "one of children not found");
            }
        }

        List<timywimy.model.bo.tasks.Task> tasksResult = ((TaskRepository) repository).
                unlinkChildren(parentTask, tasksToUnlink, userBySession.getId());

        List<Task> res = new ArrayList<>();
        for (timywimy.model.bo.tasks.Task task : tasksResult) {
            res.add(Converter.taskEntityToTaskDTO(task));
        }

        return res;
    }
}
