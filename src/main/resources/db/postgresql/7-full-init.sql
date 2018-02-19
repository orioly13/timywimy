--random admin
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES (uuid_generate_v4(), now(), 'adminus', 'lol2@kek.cheburek', 'evan', 1, TRUE);
--random user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active)
VALUES (uuid_generate_v4(), now(), 'user', 'karlik@gmail.com', '123', 0, TRUE);
--random deleted user
INSERT INTO sec_users (id, created_ts, name, email, password, role, active, deleted_ts)
VALUES (uuid_generate_v4(), now(), 'deleted_user', 'lol@kek.cheburek', '2345', 0, TRUE, now());