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
create index bo_events_idx_date_time_zone on bo_events(owner_id, date, time, zone);
create index bo_events_idx_name on bo_events(owner_id, name);