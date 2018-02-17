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