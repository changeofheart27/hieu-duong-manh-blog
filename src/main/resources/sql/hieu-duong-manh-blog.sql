DROP SCHEMA IF EXISTS `hieu-duong-manh-blog`;

CREATE SCHEMA `hieu-duong-manh-blog`;
USE `hieu-duong-manh-blog`;

DROP TABLE IF EXISTS `users-roles`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `posts`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `posts-tags`;

--
-- Table structure for table `users`
--
CREATE TABLE `users` (
  `id` int not null auto_increment,
  `username` varchar(50) not null unique,
  `password` char(68) not null,
  `dob` date default null,
  `email` varchar(50) default null,
  `created_at` date default (curdate()),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
-- NOTE: The passwords are encrypted using BCrypt
-- A generation tool is avail at: https://www.bcryptcalculator.com/
-- Default passwords here are: test@123
--
INSERT INTO `users` (`id`, `username`, `password`, `dob`, `email`, `created_at`)
VALUES
(`id`,'hieuduongm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO','1999-07-02','hieudhanu27@gmail.com',CURDATE()),
(`id`,'tringuyenm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'tringuyenm@gmail.com',CURDATE()),
(`id`,'phuongcaot','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'phuongcaot@gmail.com',CURDATE()),
(`id`,'thanhphamt','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'thanhphamt@gmail.com',CURDATE());

--
-- Table structure for table `roles`
--
CREATE TABLE `roles` (
  `id` int not null auto_increment,
  `name` varchar(50) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `roles`
--
INSERT INTO `roles` (`id`, `name`)
VALUES
(`id`,'ROLE_USER'),
(`id`,'ROLE_AUTHOR'),
(`id`,'ROLE_ADMIN');

--
-- Table structure for table `users-roles`
--
CREATE TABLE `users-roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`), 
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users-roles`
--
INSERT INTO `users-roles` (`user_id`, `role_id`)
VALUES
(1,1),
(1,2),
(1,3),
(2,1),
(2,2),
(3,1),
(4,1);

--
-- Table structure for table `tags`
--
CREATE TABLE `tags` (
  `id` int not null auto_increment,
  `name` varchar(20) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `tags`
--
INSERT INTO `tags` (`id`, `name`)
VALUES
(`id`,'#java'),
(`id`,'#springboot'),
(`id`,'#database');

--
-- Table structure for table `posts`
--
CREATE TABLE `posts` (
  `id` int not null auto_increment,
  `title` varchar(100) not null,
  `description` varchar(200),
  `content` varchar(1000),
  `created_at` date default (curdate()),
  `user_id` int,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `posts`
--
INSERT INTO `posts` (`id`,`title`,`description`,`content`,`created_at`,`user_id`)
VALUES
(`id`,'Java 17 New Features', 'Java 17, the latest (3rd) LTS, was released on September 14, 2021. What are the new features? Find out now.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.', date '2023-09-13',1),
(`id`,'Preparing for Spring 6.0 and Spring Boot 3.0', 'The entire Spring team, and many in our community of contributors, are now preparing for the next generation of Spring. We are planning to release Spring Boot 3.0 in November 2022.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.', date '2023-09-13',1),
(`id`,'JPA, Hibernate and Spring Data JPA. What are the differences?', 'https://www.youtube.com/watch?v=xHminZ9Dxm4','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.', date '2023-09-13',1);

--
-- Table structure for table `posts-tags`
--
CREATE TABLE `posts-tags` (
  `post_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  CONSTRAINT FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`), 
  CONSTRAINT FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `posts-tags`
--
INSERT INTO `posts-tags` (`post_id`, `tag_id`)
VALUES
(1,1),
(2,2),
(3,3);
