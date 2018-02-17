create table bo_event_ext_counters(
  --base
  id            uuid not null
                constraint bo_event_ext_counters_pk_id primary key
                constraint bo_event_ext_counters_fk_id_base_id references a_base_entities(id),
  --event link
  event_id      uuid
                constraint bo_event_ext_counters_fk_event_id_bo_events_id references bo_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null
                constraint bo_event_ext_counters_event_order_positive check(event_order >= 0),
  --other
  counter       integer not null default 0
);
create index bo_event_ext_counters_idx_event_order on bo_event_ext_counters(event_id, event_order);