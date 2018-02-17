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