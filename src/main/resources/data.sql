INSERT INTO users (id, cash) VALUES (1, 44);
INSERT INTO users (id, cash) VALUES (2, 1000);
INSERT INTO users (id, cash) VALUES (3, 758);
INSERT INTO users (id, cash) VALUES (4, 51236);
INSERT INTO users (id, cash) VALUES (5, 3691);

INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 1, 4000, 1, '2016-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 1, 5000, 1, '2018-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 1, 650, 0, '2018-06-27');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 1, 340, 1, '2020-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 2, 550, 0, '2016-06-21');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 3, 100, 1, '2016-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 4, 700, 0, '2016-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 2, 3600, 0, '2016-06-22');
INSERT INTO operations (id, user_id, cash, type, date) VALUES (NEXTVAL('operation_id_seq'), 1, 900, 1, '2016-06-22');