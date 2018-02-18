CREATE TABLE bo_schedules (
  --base
  id          UUID CONSTRAINT bo_schedules_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_schedules_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_schedules_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_schedules_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_schedules_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_schedules_fk_owner_id_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --durable
  duration    TIMESTAMP WITHOUT TIME ZONE,
  --other fields
  cron        VARCHAR(20)
);
CREATE INDEX bo_schedules_idx_owner_id
  ON bo_schedules (owner_id);
CREATE INDEX bo_schedules_idx_name
  ON bo_schedules (owner_id, name);