--root (and API)
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3088b1fc-43c2-4951-8b78-1f56261c16ca', now(), 'root', 'lol@kek.cheburek',
        '1000:1666aaece4953f9d2b4897f191107b14:00582c3030525f2cbec0e3b0d426f93a60a181c864b6e2c7fde5eb470a0736f419ec280591e64b1c8e882a52ed72bbe86baa7508d3c1df99206249d8b285f65c',
        1, TRUE);
--admin
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7231e-1996-11e8-accf-0ed5f89f718b', now(), 'admin', 'admin@user.test.com',
        '1000:0c4d266d00fd05c456aa86bb3938bd38:eab716eb74f5e20fa039be7812aa3f59d002bb3789d089d788bb15b541d29b12dc52f778bfe24a7a2c06f86f77edebc46d6885caf63a82a4f376dcf0fe7d4599',
        1, TRUE);
--user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f725c6-1996-11e8-accf-0ed5f89f718b', now(), 'user', 'user@user.com',
        '1000:8109f5df0be9f257d78dfa25eda0b4f2:8d48d965aa6128666e224002928e0f8851420977613e4883b5d804d0061753a255dc51570aa6f9b3e878be77975d51faec969480a94d24814a71d2928f5d8bb1',
        0, TRUE);
--inactive user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7294a-1996-11e8-accf-0ed5f89f718b', now(), 'inactive_user', 'inactive_user@user.com',
        '1000:8109f5df0be9f257d78dfa25eda0b4f2:8d48d965aa6128666e224002928e0f8851420977613e4883b5d804d0061753a255dc51570aa6f9b3e878be77975d51faec969480a94d24814a71d2928f5d8bb1',
        0, FALSE);
--invalid role user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('ea7d161f-743a-4863-9277-770a95591d3e', now(), 'invalid_role_user', 'invalid_role_user@user.test.com',
        '1000:8109f5df0be9f257d78dfa25eda0b4f2:8d48d965aa6128666e224002928e0f8851420977613e4883b5d804d0061753a255dc51570aa6f9b3e878be77975d51faec969480a94d24814a71d2928f5d8bb1',
        14, TRUE);