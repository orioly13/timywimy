package timywimy.util;

import timywimy.model.security.User;

import java.util.UUID;

public class UserTestData {
    public static final UUID ROOT_ID = UUID.fromString("3088b1fc-43c2-4951-8b78-1f56261c16ca");
    public static final UUID ADMIN_ID = UUID.fromString("19f7231e-1996-11e8-accf-0ed5f89f718b");
    public static final UUID USER_ID = UUID.fromString("19f725c6-1996-11e8-accf-0ed5f89f718b");
    public static final UUID DELETED_USER_ID = UUID.fromString("19f7294a-1996-11e8-accf-0ed5f89f718b");
    public static final UUID NO_EXISTENT_USER_ID = UUID.fromString("d6e55696-ab79-4b81-8bd1-d5aa7f3f2fe4");
    public static final UUID INVALID_ROLE_USER_ID = UUID.fromString("ea7d161f-743a-4863-9277-770a95591d3e");

    private static final User NEW_USER;

    static {
        NEW_USER = new User();
        NEW_USER.setName("New User");
        NEW_USER.setPassword("new_user_pass");
        NEW_USER.setEmail("new_user@user.test");
    }

    public static User getNewUser() {
        User res = new User();
        res.setName(NEW_USER.getName());
        res.setPassword(NEW_USER.getPassword());
        res.setEmail(NEW_USER.getEmail());
        return res;
    }


    private UserTestData() {
    }
}
