package timywimy.model.bo.tasks;

import timywimy.model.common.AbstractDefaultEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "bo_task_groups",
        indexes = {@Index(name = "bo_task_groups_idx_name", columnList = "owner_id,name")})
public class TaskGroupImpl extends AbstractDefaultEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskGroupImpl parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Collection<TaskGroupImpl> children;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<TaskImpl> tasks;


    public TaskGroupImpl getParent() {
        return parent;
    }

    public void setParent(TaskGroupImpl parent) {
        this.parent = parent;
    }

    public Collection<TaskGroupImpl> getChildren() {
        return children;
    }


    public void setChildren(Collection<TaskGroupImpl> children) {
        this.children = children;
    }

    public Collection<TaskImpl> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<TaskImpl> tasks) {
        this.tasks = tasks;
    }
}
