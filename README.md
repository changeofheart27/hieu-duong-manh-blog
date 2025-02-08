# HIEU DUONG MANH BLOG (BACKEND)

Welcome to **Hieu Duong Manh Blog**! This is a personal blog website created to share thoughts, ideas, and articles on a variety of topics such as technology, development, personal experiences, and more. It’s a place for me to connect with others and share my journey.
## Features

- **Post Management**: Perform CRUD operations (Create, Read, Update, Delete) on blog posts.
- **User Management**: Manage user registration, login, and authentication.
- **Database Integration**: Store blog posts, users, and other related data using **Spring Data JPA** with a relational database.
- **RESTful API**: Expose a REST API for interacting with the blog data.
- **JWT Authentication**: Secure the API with JSON Web Tokens (JWT).

## Tech Stack

- **Java 17**: The primary programming language, leveraging the latest features and improvements in Java.
- **Spring Boot 3**: A modern, production-ready framework to build web applications quickly and easily.
- **Spring Data JPA**: For efficient and scalable database operations, using Java Persistence API (JPA) to interact with MySQL and H2 databases.
- **Spring Security**: Used for securing the application, implementing authentication and authorization mechanisms.
- **JWT (JSON Web Token)**: Used for implementing stateless authentication with token-based user sessions.
- **MySQL**: A relational database management system used for storing blog posts, user data, and other application-related information.
- **H2**: A lightweight, in-memory database used for development and testing purposes.
- **Spring AOP (Aspect-Oriented Programming)**: Used to manage cross-cutting concerns such as logging, security, and transaction management.
- **JUnit 5**: A testing framework for writing and executing tests.
- **Mockito**: A popular mocking framework used in conjunction with JUnit 5 to test components in isolation.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/hieu-duong-manh-blog-backend.git
    ```

2. Navigate to the project directory:
    ```bash
    cd hieu-duong-manh-blog-backend
    ```

3. Build the project:
    - If using Maven:
      ```bash
      mvn clean install
      ```
    - If using Gradle:
      ```bash
      gradle build
      ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```
   Or:
    ```bash
    gradle bootRun
    ```

5. The application will be running locally on `http://localhost:8080`.

