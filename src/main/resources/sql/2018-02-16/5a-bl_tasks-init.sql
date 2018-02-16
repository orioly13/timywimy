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