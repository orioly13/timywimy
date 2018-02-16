package com.timywimy.model.bo.tasks;

import com.timywimy.model.common.DefaultEntityImpl;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class GroupImpl extends DefaultEntityImpl implements Group {

    private Group parent;
    private List<Group> children;
    private List<Task> tasks;

    @Override
    public Group getParent() {
        return parent;
    }

    @Override
    public void setParent(Group parent) {
        this.parent = parent;
    }

    @Override
    public List<Group> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Group> children) {
        this.children = children;
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
