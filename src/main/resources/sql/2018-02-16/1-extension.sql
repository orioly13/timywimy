--as postgres user
--allows to use generate uuid function
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- primary key pattern: <table>_pk_<column>
-- id uuid constraint sec_users_pk_id primary key

-- positive version pattern: <table>_version check(version >= 0)
-- version integer not null default 0 constraint sec_users_version check(version >= 0),

-- unique constraint <table>_uk_<column>
-- email  varchar(50) constraint sec_users_uq_email unique,

-- foreign key pattern: <table>_fk_<column>_<table_2>_<column in table 2>
-- owner uuid not null constraint bl_schedules_fk_owner_sec_users_id references sec_users(id)

-- index pattern: <table>_idx_<columns>
-- create index bl_schedules_idx_name on bl_schedules(name)