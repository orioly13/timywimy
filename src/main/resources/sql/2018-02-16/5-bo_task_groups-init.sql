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