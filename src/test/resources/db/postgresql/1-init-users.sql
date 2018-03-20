--root (and API)
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('3088b1fc-43c2-4951-8b78-1f56261c16ca', now(), 'root', 'lol@kek.cheburek',
        '1000:35767285604180af14f3f691200f1cf6:40fcc9380313ae64c16175fa0aed66162fb3d04fcb1fdfa5663b3ce15e509a7a50f19a9622f56cec9f89dc3da8d96a5f2aab3c4a07a3128910ea69a28cb3b6e9',
        1, TRUE);
--admin
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7231e-1996-11e8-accf-0ed5f89f718b', now(), 'admin', 'admin@user.test.com',
        '1000:40d2a6110dccf0e055d5089ec4eb3ae3:38eafb98e06187ac0c4e37ff1a7a2e7b3d6077ae0d824f9f36d9bc1111cb34b37a5bee6bfbc685ba647d9bb52372a1e7e4271c5a76f6ce0a5c3a9212f0b535ca',
        1, TRUE);
--user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f725c6-1996-11e8-accf-0ed5f89f718b', now(), 'user', 'user@user.com',
        '1000:487377b012a1e5da6263cb9288b5c318:e5df5312c58ce01e3027702140d1376ceb5fae42f391a95b71e0fe674ecdbbf6227d457a9427f6c2c7035036d84d281ac204ff10c8543cd67433775095807dd7',
        0, TRUE);
--inactive user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('19f7294a-1996-11e8-accf-0ed5f89f718b', now(), 'inactive_user', 'inactive_user@user.com',
        '1000:487377b012a1e5da6263cb9288b5c318:e5df5312c58ce01e3027702140d1376ceb5fae42f391a95b71e0fe674ecdbbf6227d457a9427f6c2c7035036d84d281ac204ff10c8543cd67433775095807dd7',
        0, FALSE);
--invalid role user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES ('ea7d161f-743a-4863-9277-770a95591d3e', now(), 'invalid_role_user', 'invalid_role_user@user.test.com',
        '1000:487377b012a1e5da6263cb9288b5c318:e5df5312c58ce01e3027702140d1376ceb5fae42f391a95b71e0fe674ecdbbf6227d457a9427f6c2c7035036d84d281ac204ff10c8543cd67433775095807dd7',
        14, TRUE);