--task1
INSERT INTO bo_tasks (id, created_ts, created_by, owner_id, name, description, priority)
VALUES
  ('cb2f6f59-c97a-4ee5-a14f-b92bb978dcb8', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'task_1', 'task_1_desc', 2);

INSERT INTO bo_tasks (id, created_ts, created_by, owner_id, name, description, priority)
VALUES
  ('7cd34633-b989-4290-b7de-f988214aa368', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', 'task_2', 'task_2_desc', 3);

INSERT INTO bo_tasks (id, created_ts, created_by, owner_id, parent_id, name, description, priority)
VALUES
  ('ea08c491-c7a3-4209-a5fd-f14fc521a508', now(), '19f725c6-1996-11e8-accf-0ed5f89f718b',
   '19f725c6-1996-11e8-accf-0ed5f89f718b', '7cd34633-b989-4290-b7de-f988214aa368',
   'task_3', 'task_3_desc', -2);