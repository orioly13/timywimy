package com.timywimy.model.bo.tasks;

import com.timywimy.model.common.DescribedEntity;
import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.common.OwnedEntity;

import java.util.List;

public interface Group extends OwnedEntity, NamedEntity, DescribedEntity {

    Group getParent();

    void setParent(Group parent);

    List<Group> getChildren();

    void setChildren(List<Group> children);

    List<Task> getTasks();

    void setTasks(List<Task> tasks);
}
