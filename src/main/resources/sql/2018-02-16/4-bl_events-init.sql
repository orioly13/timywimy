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