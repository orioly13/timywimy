--dropping all users before
DELETE FROM sec_users;
--root (and API)
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3088b1fc-43c2-4951-8b78-1f56261c16ca', now(), 'root', 'lol@kek.cheburek', 'time@LORD', 1, TRUE);
--admin
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7231e-1996-11e8-accf-0ed5f89f718b', now(), 'admin', 'admin@user.test', 'evan', 1, TRUE);
--user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f725c6-1996-11e8-accf-0ed5f89f718b', now(), 'user', 'user@user.test', '123', 0, TRUE);
--deleted user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active, deleted_ts)
VALUES ('19f7294a-1996-11e8-accf-0ed5f89f718b', now(), 'deleted_user', 'deleted_user@user.test', '2345', 0, TRUE, now());
--invalid role user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('ea7d161f-743a-4863-9277-770a95591d3e', now(), 'invalid_role_user', 'invalid_role_user@user.test', '2345', 14, TRUE);