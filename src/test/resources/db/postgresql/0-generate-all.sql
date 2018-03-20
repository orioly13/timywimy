DELETE FROM "bo_tasks";
DELETE FROM "bo_task_groups";
DELETE FROM "bo_event_ext_tickboxes";
DELETE FROM "bo_event_ext_counters";
DELETE FROM "bo_events";
DELETE FROM "bo_schedules";
DELETE FROM "sec_users";
--USERS
--root (and API)
--t1me@LORD
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3088b1fc-43c2-4951-8b78-1f56261c16ca', now(), 'root', 'lol@kek.cheburek',
        '1000:35767285604180af14f3f691200f1cf6:40fcc9380313ae64c16175fa0aed66162fb3d04fcb1fdfa5663b3ce15e509a7a50f19a9622f56cec9f89dc3da8d96a5f2aab3c4a07a3128910ea69a28cb3b6e9',
        1, TRUE);
--admin
--#tTy13ALF
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7231e-1996-11e8-accf-0ed5f89f718b', now(), 'admin', 'admin@user.test.com',
        '1000:40d2a6110dccf0e055d5089ec4eb3ae3:38eafb98e06187ac0c4e37ff1a7a2e7b3d6077ae0d824f9f36d9bc1111cb34b37a5bee6bfbc685ba647d9bb52372a1e7e4271c5a76f6ce0a5c3a9212f0b535ca',
        1, TRUE);
--user
--P@1ui$$pass
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f725c6-1996-11e8-accf-0ed5f89f718b', now(), 'user', 'user@user.com',
        '1000:487377b012a1e5da6263cb9288b5c318:e5df5312c58ce01e3027702140d1376ceb5fae42f391a95b71e0fe674ecdbbf6227d457a9427f6c2c7035036d84d281ac204ff10c8543cd67433775095807dd7',
        0, TRUE);

--SCHEDULE
--no instances
INSERT INTO bo_schedules (id, owner_id, name, description, cron)
VALUES ('eb729e1b-f23a-4665-a4b3-eab17d517748',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', 'sched_1', 'sched_desc_1', '0 0 12 * * ? *');
--three instances (one empty,one with task,one with extension)
INSERT INTO bo_schedules (id, owner_id, name, description, cron, duration)
VALUES ('e3c86daa-e368-491f-9e94-e11c55ea6d4e',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', 'sched_2', 'sched_desc_2', '0 0 12 * * ? *', '0d1h0m0s');
--empty
INSERT INTO bo_events (id, owner_id, schedule_id, name, description, date, time, zone, duration)
VALUES ('73432aa7-0db7-4244-b5a4-c33f8dad30c4',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', 'e3c86daa-e368-491f-9e94-e11c55ea6d4e', 'sched_2_e1', 'sched_desc_2_e1',
        '2018-03-19', '00:00', 'Europe/Samara', '0d1h0m0s');
--task
INSERT INTO bo_events (id, owner_id, schedule_id, name, description)
VALUES ('66ae927d-2851-428c-a1e6-b7d665ecff8b',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', 'e3c86daa-e368-491f-9e94-e11c55ea6d4e',
        'sched_2_e2', 'sched_desc_2_e2');
INSERT INTO bo_tasks (id, owner_id, event_id, name, description, date, time, zone)
VALUES ('6a713a11-17a7-43dc-ac77-9eaaaacaf9e1',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', '66ae927d-2851-428c-a1e6-b7d665ecff8b',
        'sched_2_e2_task', 'sched_desc_2_e2_task', '2018-03-25', '00:00', 'Europe/Samara');
--extensions
INSERT INTO bo_events (id, owner_id, schedule_id, name, description)
VALUES ('5b521d5c-4353-4ab0-bfe2-04cc93aec5fc',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', 'e3c86daa-e368-491f-9e94-e11c55ea6d4e',
        'sched_2_e3', 'sched_desc_2_e3');
INSERT INTO bo_event_ext_tickboxes (id, event_id, name, active)
VALUES ('743b4cc6-8a8a-4896-af58-5f8dfb70e563',
        '5b521d5c-4353-4ab0-bfe2-04cc93aec5fc', 'sched_2_e3_tick', TRUE);
INSERT INTO bo_event_ext_counters (id, event_id, name, counter)
VALUES ('87fccf74-68d2-4bfa-8ed7-bddd6ca981f0',
        '5b521d5c-4353-4ab0-bfe2-04cc93aec5fc', 'sched_2_e3_counter', 13);

--TASKS
--parent
INSERT INTO bo_tasks (id, owner_id, name, description, date, time, zone)
VALUES ('f2dc5bf7-ed4f-4b8f-a500-608267ff6d11',
        '19f725c6-1996-11e8-accf-0ed5f89f718b',
        'task_parent', 'task_parent_desc', '2018-03-25', '00:00', 'Europe/Samara');
--child
INSERT INTO bo_tasks (id, owner_id, parent_id, name, description, priority)
VALUES ('33aa4a30-075f-49ff-a7a2-3352cb68ed01',
        '19f725c6-1996-11e8-accf-0ed5f89f718b',
        'f2dc5bf7-ed4f-4b8f-a500-608267ff6d11', 'task_child', 'task_child_desc', 2);
--admin task
INSERT INTO bo_tasks (id, owner_id, name, description, date, time, zone)
VALUES ('4273d51e-d53d-4a2a-b6ac-50f19966c82c',
        '19f7231e-1996-11e8-accf-0ed5f89f718b',
        'task_admin', 'task_admin_desc', '2018-03-25', '00:00', 'Europe/Samara');
--EVENTS
--task
INSERT INTO bo_events (id, owner_id, name, description, date)
VALUES ('3b714040-55fe-4c47-9d72-238cfa0b9aa3',
        '19f725c6-1996-11e8-accf-0ed5f89f718b',
        'event_1', 'event_1_desc', '2018-04-03');
INSERT INTO bo_tasks (id, owner_id, event_id, name, description, date, time, zone)
VALUES ('c493db88-f824-45d6-86e5-1d23e4dd58c9',
        '19f725c6-1996-11e8-accf-0ed5f89f718b', '3b714040-55fe-4c47-9d72-238cfa0b9aa3',
        'event_1_task', 'event_1_task', '2018-03-25', '00:00', 'Europe/Samara');
--extensions
INSERT INTO bo_events (id, owner_id, name, description)
VALUES ('adc1e706-4819-4c57-899f-ab3af6d80fd5',
        '19f725c6-1996-11e8-accf-0ed5f89f718b',
        'event_2', 'event_2_desc');
INSERT INTO bo_event_ext_tickboxes (id, event_id, name, active)
VALUES ('46ee5a8b-969b-4915-b489-beae22a5dfd2',
        'adc1e706-4819-4c57-899f-ab3af6d80fd5', 'event_2_tick', TRUE);
INSERT INTO bo_event_ext_counters (id, event_id, name, counter)
VALUES ('7ff856f7-478f-4844-a2cb-1d20572647db',
        'adc1e706-4819-4c57-899f-ab3af6d80fd5', 'event_2_counter', 13);