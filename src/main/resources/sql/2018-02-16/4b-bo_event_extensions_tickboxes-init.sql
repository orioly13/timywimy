create table bo_event_ext_tickboxes(
  --base
  id            uuid not null
                constraint bo_event_ext_tickboxes_pk_id primary key
                constraint bo_event_ext_tickboxes_fk_id_base_id references a_base_entities(id),
  --event link
  event_id      uuid
                constraint bo_event_ext_tickboxes_fk_event_id_bo_events_id references bo_events(id),
  --named
  name        	varchar(50),
  --ordered
  event_order  	integer not null
                constraint bo_event_ext_tickboxes_event_order_positive check(event_order >= 0),
  --other
  active        boolean not null default false
);
create index bo_event_ext_tickboxes_idx_event_order on bo_event_ext_tickboxes(event_id, event_order);