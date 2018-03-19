package timywimy.util;

import timywimy.web.dto.security.User;

import java.util.UUID;

public class PositiveTestData {
    public static final UUID USER_ROOT = UUID.fromString("3088b1fc-43c2-4951-8b78-1f56261c16ca");
    public static final UUID USER_ADMIN = UUID.fromString("19f7231e-1996-11e8-accf-0ed5f89f718b");
    public static final UUID USER_USER = UUID.fromString("19f725c6-1996-11e8-accf-0ed5f89f718b");

    public static final UUID SCHED_1 = UUID.fromString("eb729e1b-f23a-4665-a4b3-eab17d517748");
    public static final UUID SCHED_2 = UUID.fromString("e3c86daa-e368-491f-9e94-e11c55ea6d4e");

    public static final UUID SCHED_2_EVT_1 = UUID.fromString("73432aa7-0db7-4244-b5a4-c33f8dad30c4");
    public static final UUID SCHED_2_EVT_2 = UUID.fromString("66ae927d-2851-428c-a1e6-b7d665ecff8b");
    public static final UUID SCHED_2_EVT_2_TSK = UUID.fromString("6a713a11-17a7-43dc-ac77-9eaaaacaf9e1");
    public static final UUID SCHED_2_EVT_3 = UUID.fromString("5b521d5c-4353-4ab0-bfe2-04cc93aec5fc");
    public static final UUID SCHED_2_EVT_3_BOX = UUID.fromString("743b4cc6-8a8a-4896-af58-5f8dfb70e563");
    public static final UUID SCHED_2_EVT_3_CNTR = UUID.fromString("87fccf74-68d2-4bfa-8ed7-bddd6ca981f0");

    public static final UUID TSK_PARENT = UUID.fromString("f2dc5bf7-ed4f-4b8f-a500-608267ff6d11");
    public static final UUID TSK_CHILD = UUID.fromString("33aa4a30-075f-49ff-a7a2-3352cb68ed01");
    public static final UUID TSK_ADMIN = UUID.fromString("4273d51e-d53d-4a2a-b6ac-50f19966c82c");

    public static final UUID EVT_1 = UUID.fromString("3b714040-55fe-4c47-9d72-238cfa0b9aa3");
    public static final UUID EVT_1_TSK = UUID.fromString("c493db88-f824-45d6-86e5-1d23e4dd58c9");
    public static final UUID EVT_2 = UUID.fromString("adc1e706-4819-4c57-899f-ab3af6d80fd5");
    public static final UUID EVT_2_BOX = UUID.fromString("46ee5a8b-969b-4915-b489-beae22a5dfd2");
    public static final UUID EVT_2_CNTR = UUID.fromString("7ff856f7-478f-4844-a2cb-1d20572647db");


    public static final User USER_LOGIN_USER;
    public static final User USER_LOGIN_ADMIN;

    static {
        USER_LOGIN_USER = new User();
        USER_LOGIN_USER.setEmail("user@user.com");
        USER_LOGIN_USER.setPassword("P@1ui$$pass");

        USER_LOGIN_ADMIN = new User();
        USER_LOGIN_ADMIN.setEmail("admin@user.test.com");
        USER_LOGIN_ADMIN.setPassword("#tTy13ALF");
    }
}
