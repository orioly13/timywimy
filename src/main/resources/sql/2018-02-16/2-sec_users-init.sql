create table sec_users(
  --base
  id          uuid not null
              constraint sec_users_pk_id primary key
              constraint sec_users_fk_id_base_id references a_base_entities(id),
  --named
  name        varchar(50),
  --other
  email       varchar(50)
              constraint sec_users_uq_email unique not null,
  password    varchar(50) not null,
  role	      numeric(2,0),
  active      boolean not null default false
);
--root
insert into a_base_entities(id, created_ts) values(uuid_generate_v4(), now());
insert into sec_users(id, name, email, password, role, active)
    values((select base.id from a_base_entities base limit 1), 'root', 'karlikve1ik@gmail.com','time@LORD', 1, true);