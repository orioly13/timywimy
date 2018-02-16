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