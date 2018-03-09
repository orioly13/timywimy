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