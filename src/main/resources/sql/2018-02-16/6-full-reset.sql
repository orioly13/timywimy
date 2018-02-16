--as postgres user
--allows to use generate uuid function
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "bl_tasks";
DROP TABLE IF EXISTS "bl_task_groups";
DROP TABLE IF EXISTS "bl_event_ext_tickboxes";
DROP TABLE IF EXISTS "bl_event_ext_counters";
DROP TABLE IF EXISTS "bl_events";
DROP TABLE IF EXISTS "bl_schedules";
DROP TABLE IF EXISTS "sec_users";

create table sec_users(
  --base
  id          uuid constraint sec_users_pk_id primary key,
  created_by  uuid,
  created_ts  timestamp with time zone,
  updated_by  uuid,
  updated_ts  timestamp with time zone,
  deleted_by  uuid,
  deleted_ts  timestamp with time zone,
  version     integer not null default 0 constraint sec_users_version check(version >= 0),
  --named
  name        varchar(50),
  --other
  email       varchar(50) constraint sec_users_uq_email unique not null,
  password    varchar(50) not null,
  role	      numeric(2,0),
  active      boolean not null default false
);
--root
insert into sec_users(id, created_ts, name, email, password, role, active)
    values(uuid_generate_v4(), now(), 'root', 'karlikve1ik@gmail.com','time@LORD', 1, true);

create table bl_schedules(
  --base
  id          	uuid constraint bl_schedules_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_schedules_version check(version >= 0),
  --owned
  owner_id      uuid not null constraint bl_schedules_fk_owner_id_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --durable
  duration		interval,
  --other fields
  cron          varchar(20)
);
create index bl_schedules_idx_name on bl_schedules(name);

create table bl_events(
  --base
  id          	uuid constraint bl_events_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_events_version check(version >= 0),
  --owned
  owner_id      uuid not null constraint bl_events_fk_owner_id_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --durable
  duration		interval,
  --datetimezone
  date 			date,
  time			time,
  zone			varchar(20),
  --schedule link
  schedule_id   uuid constraint bl_events_fk_schedule_id_bl_schedules_id references bl_schedules(id)
);
create index bl_events_idx_date_time_zone on bl_events(date,time,zone);
create index bl_events_idx_name on bl_events(name);

create table bl_event_ext_counters(
  --base
  id          	uuid constraint bl_event_ext_counters_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_event_ext_counters_version check(version >= 0),
  --event link
  event_id      uuid constraint bl_event_ext_counters_fk_event_id_bl_events_id references bl_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null constraint bl_event_ext_counters_event_order_positive check(event_order >= 0),
  --other
  counter       integer not null default 0
);
create index bl_event_ext_counters_idx_event_order on bl_event_ext_counters(event_order);

create table bl_event_ext_tickboxes(
  --base
  id          	uuid constraint bl_event_ext_tickboxes_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_event_ext_tickboxes_version check(version >= 0),
  --event link
  event_id      uuid constraint bl_event_ext_tickboxes_fk_event_id_bl_events_id references bl_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null constraint bl_event_ext_tickboxes_event_order_positive check(event_order >= 0),
  --other
  active        boolean not null default false
);
create index bl_event_ext_tickboxes_idx_event_order on bl_event_ext_tickboxes(event_order);

create table bl_task_groups(
  --base entity
  id          	uuid constraint bl_task_groups_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_task_groups_version check(version >= 0),
  --owned
  owner_id      uuid not null constraint bl_task_groups_fk_owner_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --other
  parent_id      uuid constraint bl_task_groups_fk_parent_id_bl_task_groups_id references bl_task_groups(id)
);

create table bl_tasks (
  --base entity
  id          	uuid constraint bl_tasks_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_tasks_version check(version >= 0),
  --owned
  owner_id      uuid not null constraint bl_tasks_fk_owner_sec_users_id references sec_users(id),
  --described
  description 	varchar(255),
  --datetimezone
  date 			date,
  time			time,
  zone			varchar(20),
  --event link
  event_id      uuid constraint bl_tasks_fk_event_id_bl_events_id references bl_events(id),
  --other
  parent_id     uuid constraint bl_tasks_fk_parent_id_bl_tasks_id references bl_tasks(id),
  group_id      uuid constraint bl_tasks_fk_group_id_bl_task_groups_id references bl_task_groups(id),
  priority      numeric(1),
  completed     boolean not null default false
);
create index bl_tasks_idx_date_time_zone on bl_tasks(date,time,zone);
create index bl_tasks_idx_priority on bl_tasks(priority);