package com.timywimy.model.bo.tasks;

import com.timywimy.model.common.DefaultEntityImpl;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "bo_task_groups",
        indexes = {@Index(name = "bo_task_groups_idx_name", columnList = "owner_id,name")})
public class TaskGroupImpl extends DefaultEntityImpl implements TaskGroup {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskGroup parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Collection<TaskGroup> children;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<Task> tasks;

    @Override
    public TaskGroup getParent() {
        return parent;
    }

    @Override
    public void setParent(TaskGroup parent) {
        this.parent = parent;
    }

    @Override
    public Collection<TaskGroup> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<TaskGroup> children) {
        this.children = children;
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
