--root (and API)
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3088b1fc-43c2-4951-8b78-1f56261c16ca', now(), 'root', 'lol@kek.cheburek', 'time@LORD', 1, TRUE);
--admin
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7231e-1996-11e8-accf-0ed5f89f718b', now(), 'admin', 'admin@user.test.com', '#tTy13ALF', 1, TRUE);
--user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f725c6-1996-11e8-accf-0ed5f89f718b', now(), 'user', 'user@user.com', 'P@1ui$$pass', 0, TRUE);
--inactive user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7294a-1996-11e8-accf-0ed5f89f718b', now(), 'inactive_user', 'inactive_user@user.com', 'P@1ui$$pass', 0, FALSE);
--invalid role user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('ea7d161f-743a-4863-9277-770a95591d3e', now(), 'invalid_role_user', 'invalid_role_user@user.test.com', 'pL@se145', 14, TRUE);