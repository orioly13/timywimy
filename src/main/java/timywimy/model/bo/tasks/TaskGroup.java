package timywimy.model.bo.tasks;

import timywimy.model.common.DescribedEntity;
import timywimy.model.common.NamedEntity;
import timywimy.model.common.OwnedEntity;

import java.util.Collection;

public interface TaskGroup extends OwnedEntity, NamedEntity, DescribedEntity {

    TaskGroup getParent();

    void setParent(TaskGroup parent);

    Collection<TaskGroup> getChildren();

    void setChildren(Collection<TaskGroup> children);

    Collection<Task> getTasks();

    void setTasks(Collection<Task> tasks);
}
