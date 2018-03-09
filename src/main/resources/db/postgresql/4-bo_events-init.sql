CREATE TABLE bo_events (
  --base
  id          UUID CONSTRAINT bo_events_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_events_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_events_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
--   deleted_by  UUID CONSTRAINT bo_events_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
--   deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_events_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_events_fk_owner_id_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --durable
  duration    VARCHAR(12),
  --datetimezone
  date        DATE,
  time        TIME,
  zone        VARCHAR(20),
  --schedule link
  schedule_id UUID CONSTRAINT bo_events_fk_schedule_id_bo_schedules_id REFERENCES bo_schedules (id)
);
CREATE INDEX bo_events_idx_owner_id
  ON bo_events (owner_id);
CREATE INDEX bo_events_idx_date_time_zone
  ON bo_events (owner_id, date, time, zone);
CREATE INDEX bo_events_idx_name
  ON bo_events (owner_id, name);