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
  `dob` date,
  `email` varchar(50) not null unique,
  `enabled` boolean not null default true,
  `created_at` datetime default (now()),
  `updated_at` datetime default null,
  `avatar` varchar(255) default null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
-- NOTE: The passwords are encrypted using BCrypt
-- A generation tool is avail at: https://www.bcryptcalculator.com/
-- Default passwords here are: test@123
--
INSERT INTO `users` (`id`, `username`, `password`, `dob`, `email`, `enabled`)
VALUES
(`id`,'hieudm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO','1999-07-02','hieudhanu27@gmail.com', true),
(`id`,'trinm','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'tringuyenm@gmail.com', true),
(`id`,'phuongct','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'phuongcaot@gmail.com', true),
(`id`,'thanhpt','$2a$10$sfCjRHz8YOnpFXUe8pQxaO2hjtoERH1NHngW9mD7TA4WLkHExCRaO',null,'thanhphamt@gmail.com', true);

--
-- Table structure for table `roles`
--
CREATE TABLE `roles` (
  `id` int not null auto_increment,
  `role_name` varchar(50) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `roles`
--
INSERT INTO `roles` (`id`, `role_name`)
VALUES
(`id`,'USER'),
(`id`,'AUTHOR'),
(`id`,'ADMIN');

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
  `tag_name` varchar(20) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `tags`
--
INSERT INTO `tags` (`id`, `tag_name`)
VALUES
(`id`,'#java'),
(`id`,'#springboot'),
(`id`,'#database');

--
-- Table structure for table `posts`
--
CREATE TABLE `posts` (
  `id` int not null auto_increment,
  `title` varchar(100) not null unique,
  `description` varchar(200) not null,
  `content` text not null,
  `created_at` datetime default (now()),
  `updated_at` datetime default null,
  `user_id` int,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `posts`
--
INSERT INTO `posts` (`id`,`title`,`description`,`content`,`user_id`)
VALUES
(`id`,'Java 17 New Features', 'Java 17, the latest (3rd) LTS, was released on September 14, 2021. What are the new features? Find out now.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.',1),
(`id`,'Preparing for Spring 6.0 and Spring Boot 3.0', 'The entire Spring team, and many in our community of contributors, are now preparing for the next generation of Spring. We are planning to release Spring Boot 3.0 in November 2022.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.',1),
(`id`,'JPA, Hibernate and Spring Data JPA. What are the differences?', 'https://www.youtube.com/watch?v=xHminZ9Dxm4','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed eleifend odio. Nam sodales diam efficitur convallis porttitor. In vel arcu nibh. Quisque vel volutpat urna, ac viverra neque. Suspendisse pellentesque feugiat augue. Ut porttitor purus id urna condimentum, non dapibus arcu dignissim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tincidunt magna vitae faucibus mattis. Ut lacinia nisi non quam pharetra, id cursus purus commodo. Duis et lobortis nibh, et viverra odio. Maecenas blandit posuere velit, id hendrerit leo euismod non. Suspendisse aliquet lorem libero, at maximus odio scelerisque vitae. Sed urna leo, molestie eget fermentum gravida, consequat nec lectus. Maecenas id laoreet ligula. Morbi magna tellus, fermentum non elementum at, viverra a metus. Ut sed velit sollicitudin, rhoncus nisi eu, ultricies diam.',1),
(`id`,'Spring Boot Unit testing with JUnit and Mockito?', 'In this tutorial we will learn how to perform unit testing Spring boot CRUD RESTful web services using JUnit 5 and Mockito framework.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tempor sagittis enim, a accumsan lorem laoreet sit amet. Maecenas pharetra rutrum tempus. Pellentesque dapibus dolor ut lorem sagittis pulvinar. Mauris pellentesque varius quam. Morbi viverra dolor rutrum lorem consequat scelerisque nec vitae ex. Praesent accumsan, odio ac posuere ultricies, arcu sem malesuada metus, id malesuada arcu enim eu neque. Proin at lacinia orci, at sodales est. Nulla sollicitudin placerat tristique. Vivamus a dolor at lacus pharetra faucibus. Etiam et eleifend dui. Sed finibus nibh velit, non laoreet erat lobortis non. Sed commodo metus mi, at consectetur est aliquet in. In scelerisque vestibulum metus. Sed convallis magna et bibendum finibus.',1),
(`id`,'Aspect Oriented Programming with Spring?', 'One of the key components of Spring is the AOP framework. While the Spring IoC container does not depend on AOP, it complements Spring IoC to provide a very capable middleware solution.','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tempor sagittis enim, a accumsan lorem laoreet sit amet. Maecenas pharetra rutrum tempus. Pellentesque dapibus dolor ut lorem sagittis pulvinar. Mauris pellentesque varius quam. Morbi viverra dolor rutrum lorem consequat scelerisque nec vitae ex. Praesent accumsan, odio ac posuere ultricies, arcu sem malesuada metus, id malesuada arcu enim eu neque. Proin at lacinia orci, at sodales est. Nulla sollicitudin placerat tristique. Vivamus a dolor at lacus pharetra faucibus. Etiam et eleifend dui. Sed finibus nibh velit, non laoreet erat lobortis non. Sed commodo metus mi, at consectetur est aliquet in. In scelerisque vestibulum metus. Sed convallis magna et bibendum finibus.',2),
(`id`,'Understanding Docker Containers', 'Docker containers are lightweight, portable, and powerful tools for software development. Learn all about them here.', 'Sed tristique interdum urna et ullamcorper. Vivamus a erat orci. Nunc cursus convallis ante, ut volutpat erat tincidunt eget. Integer bibendum malesuada tortor at consectetur. Nullam viverra nisi ut erat vehicula, ac volutpat ante venenatis. Ut luctus ipsum urna, a vulputate elit tempus sit amet. Cras eget tincidunt nulla, non euismod sapien. Donec convallis placerat metus, vel ultricies tortor maximus eu. Duis in velit libero. Integer sit amet posuere felis. Aenean malesuada ligula erat, vel vehicula ante scelerisque nec.', 3),
(`id`,'The Rise of Machine Learning', 'Machine learning is reshaping industries. Explore the different types of machine learning and its applications.', 'Cras non magna at libero interdum vestibulum. Sed varius, libero in bibendum facilisis, erat felis tempus risus, non varius justo libero id sapien. Nam eget libero ut nunc tristique facilisis. Etiam vel tristique dui. Integer fermentum lorem in felis sodales, id pretium mauris aliquam. Aliquam erat volutpat. Donec tempus risus quis nisi vulputate, at egestas enim pretium. Pellentesque ut lectus vitae purus tristique lobortis. Mauris et leo sit amet purus maximus ullamcorper.', 4),
(`id`,'Exploring Quantum Computing', 'Quantum computing is a revolutionary field that promises to solve problems impossible for classical computers.', 'Phasellus sit amet risus sit amet purus scelerisque fermentum ut in dui. Fusce eget eros tristique, maximus lorem sit amet, auctor risus. Morbi bibendum lobortis erat in convallis. Cras feugiat enim quis leo volutpat, eget auctor metus dictum. Mauris condimentum malesuada felis sit amet fringilla. Curabitur dictum ultricies est, id lacinia risus iaculis a. Aliquam condimentum nunc ac gravida aliquam. Aenean posuere orci et felis rhoncus aliquet.', 1),
(`id`,'Introduction to Cloud Computing', 'Cloud computing is revolutionizing how businesses operate. Discover the benefits and types of cloud solutions.', 'Nulla facilisi. Sed ut orci felis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer nec lacus lectus. Donec in risus nec metus vestibulum egestas. Fusce laoreet metus vel urna tempor, et tincidunt libero tincidunt. Morbi ultricies justo sit amet maximus tincidunt. Quisque hendrerit risus at lacus tincidunt, nec pretium risus vestibulum. Aenean eget felis sed lorem elementum varius non et ante.', 2),
(`id`,'Python for Data Science', 'Python is the top language for data science. Here are key libraries and tools every data scientist should know.', 'Aenean in sem sit amet urna interdum facilisis eget eu nisl. Fusce posuere malesuada nulla, eu tincidunt eros posuere non. Donec scelerisque erat id quam volutpat, non cursus ligula maximus. Nullam tincidunt, libero nec auctor finibus, ligula lorem auctor dui, vitae volutpat mauris felis sit amet neque. Sed ac augue a ante volutpat laoreet vel vel purus. Sed at quam quis sapien hendrerit tempor.', 3),
(`id`,'Introduction to Blockchain', 'Blockchain technology is the backbone of cryptocurrencies. Learn how it works and its potential applications.', 'Mauris venenatis nisl sit amet sapien malesuada, ut interdum neque cursus. Aenean ut est erat. Proin at neque vel lectus rhoncus sodales. Ut lobortis ligula a erat faucibus, id auctor magna vehicula. Nullam tincidunt, magna ut volutpat facilisis, tortor sapien elementum orci, non feugiat ligula nisl eget dui. Integer pretium mi sit amet orci auctor, vel viverra urna faucibus.', 4),
(`id`,'The Future of Autonomous Vehicles', 'Self-driving cars are changing the way we think about transportation. What does the future hold for this technology?', 'Vestibulum condimentum lorem a interdum efficitur. Curabitur cursus turpis eu sollicitudin congue. Vivamus id neque quam. Integer laoreet ex eget nulla sollicitudin, et gravida libero pharetra. Donec ut neque id arcu dignissim convallis. Fusce in ante sed urna iaculis vehicula. Ut varius magna eu mauris pretium, at malesuada turpis luctus. Nam ac risus non turpis tincidunt dignissim.', 1),
(`id`,'Cybersecurity in the Modern Era', 'Cybersecurity is more important than ever. Find out how to protect yourself from the latest threats.', 'Nulla vehicula orci eu libero placerat, non varius tortor feugiat. Etiam euismod, magna ut facilisis feugiat, enim felis tristique felis, sit amet aliquam sem metus sed eros. Aenean ac erat sit amet metus aliquet maximus. Curabitur sed odio non orci aliquam tempor. Morbi tristique vel nulla at placerat. Fusce at elit at ante lacinia tempor vel sit amet orci. Etiam lacinia tristique volutpat. Pellentesque tincidunt felis ut metus vehicula, nec hendrerit orci suscipit.', 2),
(`id`,'Getting Started with React.js', 'React.js is one of the most popular front-end libraries. Learn how to start building with it today.', 'Cras hendrerit urna ut nisi aliquam, et scelerisque velit posuere. Integer malesuada lectus felis, eu posuere sapien cursus nec. Vivamus gravida mauris id lectus cursus, sit amet mollis libero facilisis. In cursus dui et neque suscipit, a iaculis ante varius. Nulla ac felis a ante vestibulum aliquet et non ligula. Proin efficitur, odio at rutrum rhoncus, velit ante tincidunt augue, ac efficitur lectus augue et orci.', 3),
(`id`,'The Rise of Artificial Intelligence', 'Artificial Intelligence is transforming industries. Discover its potential and impact on the future.', 'Quisque eget felis non lectus cursus suscipit at non enim. Mauris id lorem neque. Ut tristique ex et enim laoreet, eget tempus velit maximus. Sed scelerisque lorem vitae nunc rutrum tristique. Curabitur sed metus justo. Aenean at dui eget augue faucibus interdum. Aliquam viverra ante vitae ante facilisis, id cursus risus tincidunt. Vivamus venenatis feugiat magna, at euismod risus varius nec.', 4);


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
(3,3),
(4,2),
(5,2);

--
-- Table structure for table `refresh_tokens`
--
CREATE TABLE `refresh_tokens` (
    `id` int auto_increment primary key,
    `user_id` int not null,
    `token` text not null unique,
    `expiry_date` date,
    `revoked` boolean default true,
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);
