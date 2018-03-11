package timywimy.util;


import timywimy.model.bo.tasks.Task;

import java.util.UUID;

public class TaskTestData {
    public static final UUID TASK_1 = UUID.fromString("cb2f6f59-c97a-4ee5-a14f-b92bb978dcb8");
    public static final UUID TASK_2 = UUID.fromString("7cd34633-b989-4290-b7de-f988214aa368");
    public static final UUID TASK_3 = UUID.fromString("ea08c491-c7a3-4209-a5fd-f14fc521a508");

    private static final timywimy.model.bo.events.Event TASK_NEW;
    private static final timywimy.model.bo.events.Event TASK_EXISTING;
    private static final timywimy.model.bo.events.Event TASK_EXISTING_WITH_TASKS;

    static {
        TASK_NEW = new timywimy.model.bo.events.Event();
        TASK_NEW.setName("new task");
        TASK_NEW.setDescription("new desc");
        TASK_NEW.setOwner(UserTestData.getExistingUser());
//        EVENT_NEW.setEmail("new_user@user.com");
//
        TASK_EXISTING = new timywimy.model.bo.events.Event();
        TASK_EXISTING.setId(TASK_1);
        TASK_EXISTING.setName("task_1");
        TASK_EXISTING.setOwner(UserTestData.getExistingUser());
//        EXISTING_USER.setEmail("user@user.com");

        TASK_EXISTING_WITH_TASKS = new timywimy.model.bo.events.Event();
        TASK_EXISTING_WITH_TASKS.setId(TASK_2);
        TASK_EXISTING_WITH_TASKS.setName("task_2");
        TASK_EXISTING_WITH_TASKS.setOwner(UserTestData.getExistingUser());
    }

    public static Task getTaskNew() {
        Task res = new Task();
        res.setName(TASK_NEW.getName());
        res.setDescription(TASK_NEW.getDescription());
        res.setOwner(TASK_NEW.getOwner());
        return res;
    }

    public static Task getTaskExisting() {
        Task res = new Task();
        res.setId(TASK_EXISTING.getId());
        res.setName(TASK_EXISTING.getName());
        res.setOwner(TASK_EXISTING.getOwner());
        return res;
    }

    public static Task getTaskExistingWithTasks() {
        Task res = new Task();
        res.setId(TASK_EXISTING_WITH_TASKS.getId());
        res.setName(TASK_EXISTING_WITH_TASKS.getName());
        res.setOwner(TASK_EXISTING_WITH_TASKS.getOwner());
        return res;
    }

    private TaskTestData() {
    }
}
