package com.timywimy.model.bo.tasks;

import com.timywimy.model.common.DescribedEntity;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.Collection;
import java.util.List;

public interface TaskGroup extends OwnedEntity, NamedEntity, DescribedEntity {

    TaskGroup getParent();

    void setParent(TaskGroup parent);

    Collection<TaskGroup> getChildren();

    void setChildren(Collection<TaskGroup> children);

    Collection<Task> getTasks();

    void setTasks(Collection<Task> tasks);
}
