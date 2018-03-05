CREATE TABLE sec_users (
  --base
  id          UUID CONSTRAINT sec_users_pk_id PRIMARY KEY,
  created_by  UUID CONSTRAINT sec_users_fk_created_by_sec_users_id REFERENCES sec_users (id),
  created_ts  TIMESTAMP WITH TIME ZONE,
  updated_by  UUID CONSTRAINT sec_users_fk_updated_by_sec_users_id REFERENCES sec_users (id),
  updated_ts  TIMESTAMP WITH TIME ZONE,
--   deleted_by  UUID CONSTRAINT sec_users_fk_deleted_by_sec_users_id REFERENCES sec_users (id),
--   deleted_ts  TIMESTAMP WITH TIME ZONE,
  version     INTEGER     NOT NULL DEFAULT 0 CONSTRAINT sec_users_version CHECK (version >= 0),
  --named
  name        VARCHAR(50),
  --other
  email       VARCHAR(50) NOT NULL CONSTRAINT sec_users_uq_email UNIQUE,
  password    VARCHAR(50) NOT NULL,
  role        NUMERIC(2, 0),
  active      BOOLEAN     NOT NULL DEFAULT FALSE,
  banned      BOOLEAN     NOT NULL DEFAULT FALSE,
  banned_by   UUID CONSTRAINT sec_users_fk_banned_by_sec_users_id REFERENCES sec_users (id),
  banned_till TIMESTAMP WITH TIME ZONE
);
--root
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3fca3433-9dbb-4654-b0fb-2b6abfea72ff', now(), 'root', 'lol@kek.cheburek', 'time@LORD', 1, TRUE);