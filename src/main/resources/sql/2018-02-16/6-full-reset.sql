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

create table a_base_entities(
  id          uuid
              constraint a_base_entities_pk_id primary key,
  created_by  uuid,
  created_ts  timestamp with time zone,
  updated_by  uuid,
  updated_ts  timestamp with time zone,
  deleted_by  uuid,
  deleted_ts  timestamp with time zone,
  version     integer not null default 0
              constraint a_base_entities_version check(version >= 0)
);

create table sec_users(
  --base
  id          uuid not null
              constraint sec_users_pk_id primary key
              constraint sec_users_fk_id_base_id references a_base_entities(id),
  --named
  name        varchar(50),
  --other
  email       varchar(50)
              constraint sec_users_uq_email unique not null,
  password    varchar(50) not null,
  role	      numeric(2,0),
  active      boolean not null default false
);
--root
insert into a_base_entities(id, created_ts) values(uuid_generate_v4(), now());
insert into sec_users(id, name, email, password, role, active)
    values((select base.id from a_base_entities base limit 1), 'root', 'karlikve1ik@gmail.com','time@LORD', 1, true);

create table bo_schedules(
  --base
  id            uuid not null
                constraint bo_schedules_pk_id primary key
                constraint bo_schedules_fk_id_base_id references a_base_entities(id),
  --owned
  owner_id      uuid not null
                constraint bo_schedules_fk_owner_id_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --durable
  duration		  interval,
  --other fields
  cron          varchar(20)
);
create index bo_schedules_idx_owner_id on bo_schedules(owner_id);
create index bo_schedules_idx_name on bo_schedules(owner_id, name);

create table bo_events(
  --base
  id            uuid not null
                constraint bo_events_pk_id primary key
                constraint bo_events_fk_id_base_id references a_base_entities(id),
  --owned
  owner_id      uuid not null
                constraint bo_events_fk_owner_id_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --durable
  duration		  interval,
  --datetimezone
  date 			    date,
  time			    time,
  zone			    varchar(20),
  --schedule link
  schedule_id   uuid
                constraint bo_events_fk_schedule_id_bo_schedules_id references bo_schedules(id)
);
create index bo_events_idx_owner_id on bo_events(owner_id);
create index bo_events_idx_date_time_zone on bo_events(owner_id, date,time,zone);
create index bo_events_idx_name on bo_events(owner_id, name);

create table bo_event_ext_counters(
  --base
  id            uuid not null
                constraint bo_event_ext_counters_pk_id primary key
                constraint bo_event_ext_counters_fk_id_base_id references a_base_entities(id),
  --event link
  event_id      uuid
                constraint bo_event_ext_counters_fk_event_id_bo_events_id references bo_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null
                constraint bo_event_ext_counters_event_order_positive check(event_order >= 0),
  --other
  counter       integer not null default 0
);
create index bo_event_ext_counters_idx_event_order on bo_event_ext_counters(event_id, event_order);

create table bo_event_ext_tickboxes(
  --base
  id            uuid not null
                constraint bo_event_ext_tickboxes_pk_id primary key
                constraint bo_event_ext_tickboxes_fk_id_base_id references a_base_entities(id),
  --event link
  event_id      uuid
                constraint bo_event_ext_tickboxes_fk_event_id_bo_events_id references bo_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null
                constraint bo_event_ext_tickboxes_event_order_positive check(event_order >= 0),
  --other
  active        boolean not null default false
);
create index bo_event_ext_tickboxes_idx_event_order on bo_event_ext_tickboxes(event_id, event_order);


create table bo_task_groups(
  --base
  id            uuid not null
                constraint bo_task_groups_pk_id primary key
                constraint bo_task_groups_fk_id_base_id references a_base_entities(id),
  --owned
  owner_id      uuid not null
                constraint bo_task_groups_fk_owner_sec_users_id references sec_users(id),
  --named
  name        	varchar(50),
  --described
  description 	varchar(255),
  --other
  parent_id     uuid
                constraint bo_task_groups_fk_parent_id_bo_task_groups_id references bo_task_groups(id)
);

create table bo_tasks (
  --base
  id            uuid not null
                constraint bo_tasks_pk_id primary key
                constraint bo_tasks_fk_id_base_id references a_base_entities(id),
  --owned
  owner_id      uuid not null
                constraint bo_tasks_fk_owner_sec_users_id references sec_users(id),
  --described
  description 	varchar(255),
  --datetimezone
  date 			date,
  time			time,
  zone			varchar(20),
  --event link
  event_id      uuid
                constraint bo_tasks_fk_event_id_bo_events_id references bo_events(id),
  --other
  parent_id     uuid
                constraint bo_tasks_fk_parent_id_bo_tasks_id references bo_tasks(id),
  group_id      uuid
                constraint bo_tasks_fk_group_id_bo_task_groups_id references bo_task_groups(id),
  priority      numeric(1),
  completed     boolean not null default false
);
create index bo_tasks_idx_date_time_zone on bo_tasks(owner_id, date, time, zone);
create index bo_tasks_idx_priority on bo_tasks(owner_id, priority);