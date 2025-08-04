# Bulletin Board Project

A simple Spring Boot-based web bulletin board application supporting post creation, editing, deletion, and comments with login functionality.

## Features

- User registration / login / logout
- Create, read, update, delete posts
- Write, edit, delete comments
- Only authenticated users can write posts
- Only the author can edit or delete their content
- Input validation and exception handling
- RESTful API structure (or standard MVC-based architecture)

## Tech Stack

- Backend: Spring Boot, Spring MVC, JPA (Hibernate)
- Frontend: HTML, CSS, JavaScript
- Database: h2
- Build Tool: Gradle (or Maven)

## Member

- HEO Sunghak: Built Backend API Server, Frontend except for CSS and design, and database h2.
- ChatGPT: Supported with CSS and design, UI.
  
## Project Structure

```plaintext
src
└── main
    ├── java
    │   └── com.sunghak.board
    │       ├── apicontroller
    │       │   ├── CommentApiController
    │       │   ├── MemberApiController
    │       │   └── PostApiController
    │       ├── controller
    │       │   ├── CommentController
    │       │   ├── HomeController
    │       │   ├── MemberController
    │       │   └── PostController
    │       ├── dto
    │       │   ├── *Request, *DTO, SessionMember
    │       ├── entity
    │       │   ├── BaseTimeEntity, Comment, Member, Post
    │       ├── exception
                ├──GlobalExceptionHandler
    │       ├── repository
    │       │   ├── *Repository interfaces
    │       ├── service
    │       │   ├── *Service interfaces & *ServiceImpl classes
    │       └── BoardApplication
    └── resources
        ├── static
        │   ├── css/
        │   ├── js/
        │   └── member/, post/, comment/
        └── application.properties
```


## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/bulletin-board.git
   cd bulletin-board
   ```

2. Open the project in **IntelliJ IDEA**.

3. Make sure the project has the correct dependencies in `build.gradle`:
   ```gradle
   implementation 'com.h2database:h2'
   ```

   > No need to install H2 separately. It will be downloaded automatically via Gradle.

4. The project uses an **H2 file-based database** located at:
   ```
   ~/boarddb.mv.db
   ```
   - This file will be automatically created in the **home directory** of your system (e.g., `/Users/yourname/boarddb.mv.db` on macOS/Linux, `C:\Users\yourname\boarddb.mv.db` on Windows).
   - You do **not** need to manually create or download it.

5. The `application.properties` is located in:
   ```
   src/main/resources/application.properties
   ```
   and includes the following H2 settings:
   ```properties
   spring.datasource.url=jdbc:h2:tcp://localhost/~/boarddb
   spring.datasource.driver-class-name=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=

   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   ```

6. Run the application:
   - In IntelliJ, open `Application.java`
   - Right-click and select **Run 'Application.main()'**

7. Open your browser and access the H2 database console:
   ```
   http://localhost:8080/h2-console
   ```
   Use the following credentials:
   - **JDBC URL**: `jdbc:h2:tcp://localhost/~/boarddb`
   - **User Name**: `sa`
   - **Password**: *(leave blank)*

8. (Optional) You can also run the application from the command line:
   ```bash
   ./gradlew bootRun
   ```
   
9. Open your browser and access the application at:
   ```
   http://localhost:8080/
   ```
   
10. To access the H2 database console, go to:
   ```
   http://localhost:8080/h2-console
   ```
   Use the following credentials:
   - **JDBC URL**: `jdbc:h2:tcp://localhost/~/boarddb`
   - **User Name**: `sa`
   - **Password**: *(leave blank)*

## Purpose

This project was built to practice backend development using Spring Boot, focusing on understanding MVC patterns, REST APIs, authentication, and CRUD operations.
