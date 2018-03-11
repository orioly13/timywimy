package timywimy.util;

import timywimy.model.bo.events.Schedule;

import java.util.UUID;

public class ScheduleTestData {
    public static final UUID SCHEDULE_1 = UUID.fromString("d9fb7e14-9035-41fd-b51c-eb96c36a464b");
    public static final UUID SCHEDULE_2 = UUID.fromString("dd6d1e45-eee1-4047-8284-daa9bf27a58d");
    public static final UUID SCHEDULE_3 = UUID.fromString("1ee06a6b-1f47-4d7f-a21a-f300cea47cb0");

    private static final Schedule SCHEDULE_NEW;
    private static final Schedule SCHEDULE_EXISTING;
    private static final Schedule SCHEDULE_EXISTING_WITH_EVENTS;
    private static final Schedule SCHEDULE_EXISTING_WITH_EVENTS_TASKS;

    static {
        SCHEDULE_NEW = new Schedule();
        SCHEDULE_NEW.setName("new schedule");
        SCHEDULE_NEW.setDescription("new sched desc");
        SCHEDULE_NEW.setOwner(UserTestData.getExistingUser());
//        EVENT_NEW.setEmail("new_user@user.com");
//
        SCHEDULE_EXISTING = new Schedule();
        SCHEDULE_EXISTING.setId(SCHEDULE_1);
        SCHEDULE_EXISTING.setName("schedule_1");
        SCHEDULE_EXISTING.setOwner(UserTestData.getExistingUser());
//        EXISTING_USER.setEmail("user@user.com");

        SCHEDULE_EXISTING_WITH_EVENTS = new Schedule();
        SCHEDULE_EXISTING_WITH_EVENTS.setId(SCHEDULE_2);
        SCHEDULE_EXISTING_WITH_EVENTS.setName("schedule_2");
        SCHEDULE_EXISTING_WITH_EVENTS.setOwner(UserTestData.getExistingUser());

        SCHEDULE_EXISTING_WITH_EVENTS_TASKS = new Schedule();
        SCHEDULE_EXISTING_WITH_EVENTS_TASKS.setId(SCHEDULE_3);
        SCHEDULE_EXISTING_WITH_EVENTS_TASKS.setName("schedule_3");
        SCHEDULE_EXISTING_WITH_EVENTS_TASKS.setOwner(UserTestData.getExistingUser());
    }

    public static Schedule getScheduleNew() {
        Schedule res = new Schedule();
        res.setName(SCHEDULE_NEW.getName());
        res.setDescription(SCHEDULE_NEW.getDescription());
        res.setOwner(SCHEDULE_NEW.getOwner());
        return res;
    }

    public static Schedule getScheduleExisting() {
        Schedule res = new Schedule();
        res.setId(SCHEDULE_EXISTING.getId());
        res.setName(SCHEDULE_EXISTING.getName());
        res.setOwner(SCHEDULE_EXISTING.getOwner());
        return res;
    }

    public static Schedule getScheduleExistingWithEvents() {
        Schedule res = new Schedule();
        res.setId(SCHEDULE_EXISTING_WITH_EVENTS.getId());
        res.setName(SCHEDULE_EXISTING_WITH_EVENTS.getName());
        res.setOwner(SCHEDULE_EXISTING_WITH_EVENTS.getOwner());
        return res;
    }

    public static Schedule getScheduleExistingWithEventsTasks() {
        Schedule res = new Schedule();
        res.setId(SCHEDULE_EXISTING_WITH_EVENTS_TASKS.getId());
        res.setName(SCHEDULE_EXISTING_WITH_EVENTS_TASKS.getName());
        res.setOwner(SCHEDULE_EXISTING_WITH_EVENTS_TASKS.getOwner());
        return res;
    }

    private ScheduleTestData() {
    }
}
