DELETE FROM bo_event_ext_counters;
DELETE FROM bo_event_ext_tickboxes;
DELETE FROM bo_events;
--dropping all users before
DELETE FROM sec_users;
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

--full event
INSERT INTO bo_events (id, created_ts, created_by, owner_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('ae57eab7-07c1-4dbe-a3fc-49398ed1c1ae', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'event_1', 'event_1_desc', '2018-03-07', '23:12:00', 'Europe/Samara',
   '1d2h3m5s');
--no duration event
INSERT INTO bo_events (id, created_ts, created_by, owner_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('62226b5a-3ab6-496b-94bf-93351aaf6508', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'event_2', 'event_2_desc', '2018-03-07', '23:12:00', 'Europe/Samara', NULL);
--no zone event
INSERT INTO bo_events (id, created_ts, created_by, owner_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('08d60cfd-aa80-473a-b1a4-015f2a28300c', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'event_3', 'event_3_desc', '2018-03-07', '23:12:00', NULL, NULL);

--no zone event
INSERT INTO bo_events (id, created_ts, created_by, owner_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('103d9a65-0fbe-4ede-9336-7ad0187a1349', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'event_4', NULL, '2018-03-07', NULL, NULL, NULL);