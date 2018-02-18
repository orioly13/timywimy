CREATE TABLE bo_task_groups (
  --base
  id          UUID CONSTRAINT bo_task_groups_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_task_groups_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_task_groups_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
  deleted_by  UUID CONSTRAINT bo_task_groups_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
  deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_task_groups_version CHECK (version >= 0),
  --owned
  owner_id    UUID    NOT NULL CONSTRAINT bo_task_groups_fk_owner_sec_users_id REFERENCES sec_users (id),
  --named
  name        VARCHAR(50),
  --described
  description VARCHAR(255),
  --other
  parent_id   UUID CONSTRAINT bo_task_groups_fk_parent_id_bo_task_groups_id REFERENCES bo_task_groups (id)
);
CREATE INDEX bo_task_groups_idx_name
  ON bo_task_groups (owner_id, name);