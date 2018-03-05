CREATE TABLE bo_event_ext_counters (
  --base
  id          UUID CONSTRAINT bo_event_ext_counters_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT bo_event_ext_counters_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT bo_event_ext_counters_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
--   deleted_by  UUID CONSTRAINT bo_event_ext_counters_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
--   deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER NOT NULL DEFAULT 0 CONSTRAINT bo_event_ext_counters_version CHECK (version >= 0),
  --event link
  event_id    UUID CONSTRAINT bo_event_ext_counters_fk_event_id_bo_events_id REFERENCES bo_events (id),
  --named
  name        VARCHAR(50),
  --ordered
  event_order INTEGER NOT NULL CONSTRAINT bo_event_ext_counters_event_order_positive CHECK (event_order >= 0),
  --other
  counter     INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX bo_event_ext_counters_idx_event_order
  ON bo_event_ext_counters (event_id, event_order);