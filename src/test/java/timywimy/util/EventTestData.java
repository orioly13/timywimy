package timywimy.util;

import timywimy.model.bo.events.Event;

import java.util.UUID;

public class EventTestData {
    public static final UUID EVENT_1 = UUID.fromString("ae57eab7-07c1-4dbe-a3fc-49398ed1c1ae");
    public static final UUID EVENT_2 = UUID.fromString("62226b5a-3ab6-496b-94bf-93351aaf6508");
    public static final UUID EVENT_3 = UUID.fromString("08d60cfd-aa80-473a-b1a4-015f2a28300c");
    public static final UUID EVENT_4 = UUID.fromString("103d9a65-0fbe-4ede-9336-7ad0187a1349");

    private static final Event EVENT_NEW;
    private static final Event EVENT_EXISTING;

    static {
        EVENT_NEW = new Event();
        EVENT_NEW.setName("new event");
        EVENT_NEW.setOwner(UserTestData.getExistingUser());
//        EVENT_NEW.setEmail("new_user@user.com");
//
        EVENT_EXISTING = new Event();
        EVENT_EXISTING.setId(EVENT_1);
        EVENT_EXISTING.setName("event_1");
        EVENT_EXISTING.setOwner(UserTestData.getExistingUser());
//        EXISTING_USER.setEmail("user@user.com");
    }

    public static Event getEventNew() {
        Event res = new Event();
        res.setName(EVENT_NEW.getName());
        res.setOwner(EVENT_NEW.getOwner());
        return res;
    }

//    public static timywimy.web.dto.User getNewUserDTO() {
//        timywimy.web.dto.User res = new timywimy.web.dto.User();
//        res.setName(NEW_USER.getName());
//        res.setPassword(NEW_USER.getPassword());
//        res.setEmail(NEW_USER.getEmail());
//        return res;
//    }

    public static Event getEventExisting() {
        Event res = new Event();
        res.setId(EVENT_EXISTING.getId());
        res.setName(EVENT_EXISTING.getName());
        res.setOwner(EVENT_EXISTING.getOwner());
        return res;
    }
//
//    public static timywimy.web.dto.User getExistingUserDTO() {
//        timywimy.web.dto.User res = new timywimy.web.dto.User();
//        res.setName(EXISTING_USER.getName());
//        res.setPassword(EXISTING_USER.getPassword());
//        res.setEmail(EXISTING_USER.getEmail());
//        return res;
//    }


    private EventTestData() {
    }
}
