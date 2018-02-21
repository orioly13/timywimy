package timywimy.model.bo.tasks;

import timywimy.model.common.AbstractDefaultEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "bo_task_groups",
        indexes = {@Index(name = "bo_task_groups_idx_name", columnList = "owner_id,name")})
public class TaskGroup extends AbstractDefaultEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskGroup parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Collection<TaskGroup> children;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<Task> tasks;


    public TaskGroup getParent() {
        return parent;
    }

    public void setParent(TaskGroup parent) {
        this.parent = parent;
    }

    public Collection<TaskGroup> getChildren() {
        return children;
    }


    public void setChildren(Collection<TaskGroup> children) {
        this.children = children;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
