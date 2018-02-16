create table sec_users(
  --base
  id          uuid constraint sec_users_pk_id primary key,
  created_by  uuid,
  created_ts  timestamp with time zone,
  updated_by  uuid,
  updated_ts  timestamp with time zone,
  deleted_by  uuid,
  deleted_ts  timestamp with time zone,
  version     integer not null default 0 constraint sec_users_version check(version >= 0),
  --named
  name        varchar(50),
  --other
  email       varchar(50) constraint sec_users_uq_email unique not null,
  password    varchar(50) not null,
  role	      numeric(2,0),
  active      boolean not null default false
);
--root
insert into sec_users(id, created_ts, name, email, password, role, active)
    values(uuid_generate_v4(), now(), 'root', 'karlikve1ik@gmail.com','time@LORD', 1, true);