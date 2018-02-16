create table bl_event_ext_tickboxes(
  --base
  id          	uuid constraint bl_event_ext_tickboxes_pk_id primary key not null,
  created_by  	uuid,
  created_ts  	timestamp with time zone,
  updated_by  	uuid,
  updated_ts  	timestamp with time zone,
  deleted_by  	uuid,
  deleted_ts  	timestamp with time zone,
  version       integer not null default 0 constraint bl_event_ext_tickboxes_version check(version >= 0),
  --event link
  event_id      uuid constraint bl_event_ext_tickboxes_fk_event_id_bl_events_id references bl_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null constraint bl_event_ext_tickboxes_event_order_positive check(event_order >= 0),
  --other
  active        boolean not null default false
);
create index bl_event_ext_tickboxes_idx_event_order on bl_event_ext_tickboxes(event_order);