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