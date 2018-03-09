CREATE TABLE bo_tasks (
  --base
  id          UUID CONSTRAINT bo_tasks_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_tasks_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_tasks_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  --   deleted_by  UUID CONSTRAINT bo_tasks_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  --   deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_tasks_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_tasks_fk_owner_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --datetimezone
  date        DATE,
  time        TIME,
  zone        VARCHAR(20),
  --event link
  event_id    UUID CONSTRAINT bo_tasks_fk_event_id_bo_events_id REFERENCES bo_events (id),
  --other
  parent_id   UUID CONSTRAINT bo_tasks_fk_parent_id_bo_tasks_id REFERENCES bo_tasks (id),
  group_id    UUID CONSTRAINT bo_tasks_fk_group_id_bo_task_groups_id REFERENCES bo_task_groups (id),
  priority    NUMERIC(1, 0),
  completed   BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX bo_tasks_idx_date_time_zone
  ON bo_tasks (owner_id, date, time, zone);
CREATE INDEX bo_tasks_idx_priority
  ON bo_tasks (owner_id, priority);