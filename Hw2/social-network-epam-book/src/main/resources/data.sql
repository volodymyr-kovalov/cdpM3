INSERT INTO user VALUES ('user', 'name', '2000-11-11', '$2a$12$LW9U0fvksOM23CwNnFs4uuw6jA3f1KHsm/NtS7tqi8O29EyRQnVYy');
INSERT INTO user VALUES ('admin', 'name', '2000-11-11', '$2a$12$l6Xi01NSvgjBu2facSArzOEFBwgdBioneplX0SXaSZOaB42qVsn4K');
INSERT INTO role VALUES (1, 'USER');
INSERT INTO role VALUES (2, 'ADMIN');
INSERT INTO user_role VALUES ('user', 1);
INSERT INTO user_role VALUES ('admin', 2);