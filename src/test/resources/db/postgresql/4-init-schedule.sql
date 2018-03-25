--schedule1
INSERT INTO bo_schedules (id, created_ts, created_by, owner_id, name, description, cron)
VALUES
  ('d9fb7e14-9035-41fd-b51c-eb96c36a464b', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'sched_1', 'sched_1_desc', '* * * * *');
--schedule with event
INSERT INTO bo_schedules (id, created_ts, created_by, owner_id, name, description, cron)
VALUES
  ('dd6d1e45-eee1-4047-8284-daa9bf27a58d', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'sched_2', 'sched_2_desc', '* * * * *');
INSERT INTO bo_events (id, created_ts, created_by, owner_id, schedule_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('ae57eab7-07c1-4dbe-a3fc-49398ed1c1ae', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
                                           '19f725c6-1996-11e8-accf-0ed5f89f718b',
                                           'dd6d1e45-eee1-4047-8284-daa9bf27a58d', 'event_1', 'event_1_desc',
                                           '2018-03-07', '23:12:00', 'Europe/Samara',
                                           '1d2h3m5s');
--schedule with event and task
INSERT INTO bo_schedules (id, created_ts, created_by, owner_id, name, description, cron)
VALUES
  ('1ee06a6b-1f47-4d7f-a21a-f300cea47cb0', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'sched_3', 'sched_3_desc', '* * * * *');
INSERT INTO bo_events (id, created_ts, created_by, owner_id, schedule_id, "name", description, "date", "time", "zone", duration)
VALUES
  ('08d60cfd-aa80-473a-b1a4-015f2a28300c', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
                                           '19f725c6-1996-11e8-accf-0ed5f89f718b',
                                           '1ee06a6b-1f47-4d7f-a21a-f300cea47cb0', 'event_3', 'event_3_desc',
                                           '2018-03-07', '23:12:00', NULL, NULL);
INSERT INTO bo_tasks (id, created_ts, created_by, owner_id, event_id, name, description, priority)
VALUES
  ('b1dc4d9e-0e95-4dfc-b3f9-26face204d89', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '08d60cfd-aa80-473a-b1a4-015f2a28300c', 'event_task', 'task_desc', 2);