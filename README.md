# Spring Boot Board Project

A simple bulletin board web application built with Spring Boot.  
This project focuses on understanding backend web application structure, MVC patterns, and database interaction using JPA.

---

## Project Overview

This project is a **personal Spring Boot web application** that implements a basic bulletin board system.  
Users can create, view, edit, and delete posts, as well as add comments.  
The project emphasizes **clear separation of concerns**, **clean controller-service-repository structure**, and **practical handling of common web issues** such as authentication state and lazy loading.

---

## 🛠 Tech Stack

- **Backend**: Spring Boot
- **ORM**: Spring Data JPA (Hibernate)
- **Database**: H2
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA

---

## Key Features

### Post Management (CRUD)
- Create, read, update, and delete posts
- Display post details together with associated comments
- Handle post-related operations through RESTful APIs

---

### View Count
- Automatically increment view count when a post detail page is accessed
- View count logic is handled on the server side to prevent manipulation
- Maintain data consistency by updating the count and refetching the entity

---

### Like System
- Support liking posts with a dedicated like feature
- Manage like counts on the server to ensure reliability
- Update like status and count asynchronously without page reloads

---

### Comment System
- Add comments and reply to individual posts and comments
- Edit and delete comments
- Provide inline comment editing without page navigation
- Communicate with backend APIs using JavaScript for comment actions

---

### Authentication-aware UI
- Use a reusable JavaScript utility function to check login status
- Conditionally render UI elements based on authentication state, such as:
  - Create post button
  - Edit / delete buttons
  - Like button
- Separate authentication logic from UI rendering logic

---

### Frontend
- Build the frontend using HTML, CSS, and vanilla JavaScript
- Use the Fetch API for asynchronous communication
- Clearly separate frontend UI logic from backend business logic

---

### Backend Architecture
- Follow a Controller–Service–Repository layered architecture
- Encapsulate business logic within the Service layer
- Use DTOs when necessary instead of exposing entities directly

---

## Technical Considerations & Lessons Learned

### Lazy Initialization Issue
- Encountered `LazyInitializationException` when updating view counts using `@Modifying`
- Learned that clearing the persistence context can detach entities unexpectedly
- Solved by:
  - Updating the view count
  - Refetching the entity with fetch joins
  - Returning DTOs instead of entities

### Inline Editing with Thymeleaf
- Used `th:replace` with fragments to support inline comment editing
- Managed editing state using request parameters instead of JavaScript-heavy solutions

---

## How to Run

1. Open the project in IntelliJ
2. Run the Spring Boot application
3. Run H2 Database
4. Access the application at: http:/localhost:8080


---

## Purpose of the Project

- Practice Spring Boot fundamentals
- Understand MVC architecture in real-world scenarios
- Learn common pitfalls in JPA and Hibernate
- Build a maintainable backend structure for future expansion

---

## Possible Extensions

- User authentication and authorization
- Pagination and search
- REST API version
- Migration to MySQL or PostgreSQL


