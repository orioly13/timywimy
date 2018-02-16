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