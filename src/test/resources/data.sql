SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE `users-roles` RESTART IDENTITY;
TRUNCATE TABLE roles RESTART IDENTITY;
TRUNCATE TABLE posts RESTART IDENTITY;
TRUNCATE TABLE tags RESTART IDENTITY;
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE `posts-tags` RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

--
-- Inserting data for table `users`
-- NOTE: The passwords are encrypted using BCrypt
-- A generation tool is avail at: https://www.bcryptcalculator.com/
-- Default passwords here are: test@123
--
INSERT INTO users (username, password, dob, email, created_at) VALUES ('hieuduongm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO','1999-07-02','hieudhanu27@gmail.com',CURRENT_DATE);
INSERT INTO users (username, password, dob, email, created_at) VALUES ('tringuyenm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'tringuyenm@gmail.com',CURRENT_DATE);
INSERT INTO users (username, password, dob, email, created_at) VALUES ('phuongcaot','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'phuongcaot@gmail.com',CURRENT_DATE);
INSERT INTO users (username, password, dob, email, created_at) VALUES ('thanhphamt','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'thanhphamt@gmail.com',CURRENT_DATE);


--
-- Inserting data for table `roles`
--
INSERT INTO roles (id, role_name) VALUES (1,'USER');
INSERT INTO roles (id, role_name) VALUES (2,'AUTHOR');
INSERT INTO roles (id, role_name) VALUES (3,'ADMIN');

--
-- Inserting data for table `users-roles`
--
INSERT INTO `users-roles` (user_id, role_id) VALUES (1,1);
INSERT INTO `users-roles` (user_id, role_id) VALUES (1,2);
INSERT INTO `users-roles` (user_id, role_id) VALUES (1,3);
INSERT INTO `users-roles` (user_id, role_id) VALUES (2,1);
INSERT INTO `users-roles` (user_id, role_id) VALUES (2,2);
INSERT INTO `users-roles` (user_id, role_id) VALUES (3,1);
INSERT INTO `users-roles` (user_id, role_id) VALUES (4,1);

--
-- Inserting data for table `tags`
--
INSERT INTO tags (id, tag_name) VALUES (1,'#java');
INSERT INTO tags (id, tag_name) VALUES (2,'#springboot');
INSERT INTO tags (id, tag_name) VALUES (3,'#database');

--
-- Inserting data for table `posts`
--
INSERT INTO posts (title, description, content, created_at, user_id) VALUES ('Demo Post Title 1', 'Demo Post Description 1','Demo Post Content 1', CURRENT_DATE, 1);
INSERT INTO posts (title, description, content, created_at, user_id) VALUES ('Demo Post Title 2', 'Demo Post Description 2','Demo Post Content 2', CURRENT_DATE, 1);
INSERT INTO posts (title, description, content, created_at, user_id) VALUES ('Demo Post Title 3', 'Demo Post Description 3','Demo Post Content 3', CURRENT_DATE, 1);
INSERT INTO posts (title, description, content, created_at, user_id) VALUES ('Demo Post Title 4', 'Demo Post Description 4','Demo Post Content 4', CURRENT_DATE, 2);
INSERT INTO posts (title, description, content, created_at, user_id) VALUES ('Demo Post Title 5', 'Demo Post Description 5','Demo Post Content 5', CURRENT_DATE, 2);

--
-- Inserting data for table `posts-tags`
--
INSERT INTO `posts-tags` (post_id, tag_id) VALUES (1,1);
INSERT INTO `posts-tags` (post_id, tag_id) VALUES (2,2);
INSERT INTO `posts-tags` (post_id, tag_id) VALUES (3,3);
INSERT INTO `posts-tags` (post_id, tag_id) VALUES (4,1);
INSERT INTO `posts-tags` (post_id, tag_id) VALUES (5,2);
