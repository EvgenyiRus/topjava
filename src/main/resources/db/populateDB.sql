DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (dateTime,description, calories, user_id) VALUES
('2020-02-23 09:00','Завтрак User',100, 100000),
('2020-02-23 13:00','Обед User',200, 100000),
('2020-02-23 19:00','Ужин User',150, 100000),
('2020-02-24 09:00','Завтрак Admin',200, 100001),
('2020-02-24 13:00','Обед Admin',300, 100001),
('2020-02-24 19:00','Ужин Admin',250, 100001);