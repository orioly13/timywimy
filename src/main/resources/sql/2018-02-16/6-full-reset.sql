--as postgres user
--allows to use generate uuid function
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "bo_tasks";
DROP TABLE IF EXISTS "bo_task_groups";
DROP TABLE IF EXISTS "bo_event_ext_tickboxes";
DROP TABLE IF EXISTS "bo_event_ext_counters";
DROP TABLE IF EXISTS "bo_events";
DROP TABLE IF EXISTS "bo_schedules";
DROP TABLE IF EXISTS "sec_users";
DROP TABLE IF EXISTS "a_base_entities";

CREATE TABLE sec_users (
  --base
  id          UUID CONSTRAINT sec_users_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT sec_users_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT sec_users_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT sec_users_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER     NOT NULL DEFAULT 0 CONSTRAINT sec_users_version CHECK (version >= 0),
  --named
  name        VARCHAR(50),
  --other
  email       VARCHAR(50) NOT NULL CONSTRAINT sec_users_uq_email UNIQUE,
  password    VARCHAR(50) NOT NULL,
  role        NUMERIC(2, 0),
  active      BOOLEAN     NOT NULL DEFAULT FALSE,
  banned      BOOLEAN     NOT NULL DEFAULT FALSE,
  banned_by   UUID CONSTRAINT sec_users_fk_banned_by_sec_users_id REFERENCES sec_users (id),
  banned_till TIMESTAMP WITH TIME ZONE
);
--root
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES (uuid_generate_v4(), now(), 'root', 'karlikve1ik@gmail.com', 'time@LORD', 1, TRUE);

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

CREATE TABLE bo_events (
  --base
  id          UUID CONSTRAINT bo_events_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_events_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_events_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_events_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_events_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_events_fk_owner_id_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --durable
  duration    TIMESTAMP WITHOUT TIME ZONE,
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

CREATE TABLE bo_event_ext_counters (
  --base
  id          UUID CONSTRAINT bo_event_ext_counters_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_event_ext_counters_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_event_ext_counters_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_event_ext_counters_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_event_ext_counters_version CHECK (version >= 0),
  --event link
  event_id    UUID CONSTRAINT bo_event_ext_counters_fk_event_id_bo_events_id REFERENCES bo_events (id),
  --named
  name        VARCHAR(50),
  --ordered
  event_order INTEGER NOT NULL CONSTRAINT bo_event_ext_counters_event_order_positive CHECK (event_order >= 0),
  --other
  counter     INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX bo_event_ext_counters_idx_event_order
  ON bo_event_ext_counters (event_id, event_order);

CREATE TABLE bo_event_ext_tickboxes (
  --base
  id          UUID CONSTRAINT bo_event_ext_tickboxes_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_event_ext_tickboxes_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_event_ext_tickboxes_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_event_ext_tickboxes_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_event_ext_tickboxes_version CHECK (version >= 0),
  --event link
  event_id    UUID CONSTRAINT bo_event_ext_tickboxes_fk_event_id_bo_events_id REFERENCES bo_events (id),
  --named
  name        VARCHAR(50),
  --ordered
  event_order INTEGER NOT NULL CONSTRAINT bo_event_ext_tickboxes_event_order_positive CHECK (event_order >= 0),
  --other
  active      BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX bo_event_ext_tickboxes_idx_event_order
  ON bo_event_ext_tickboxes (event_id, event_order);

CREATE TABLE bo_task_groups (
  --base
  id          UUID CONSTRAINT bo_task_groups_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_task_groups_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_task_groups_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_task_groups_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_task_groups_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_task_groups_fk_owner_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --other
  parent_id   UUID CONSTRAINT bo_task_groups_fk_parent_id_bo_task_groups_id REFERENCES bo_task_groups (id)
);
CREATE INDEX bo_task_groups_idx_name
  ON bo_task_groups (owner_id, name);

CREATE TABLE bo_tasks (
  --base
  id          UUID CONSTRAINT bo_tasks_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_tasks_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_tasks_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_tasks_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_tasks_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_tasks_fk_owner_sec_users_id REFERENCES sec_users (id),
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