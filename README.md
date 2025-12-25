# FinalRPO — Online IT Courses Platform

## Project Overview
FinalRPO is a simple online learning platform where:
- **Teachers** create courses and lessons.
- **Students** enroll in courses and leave reviews.
- **Admins** manage users, roles, and account status (block/unblock).

This project follows the layered Spring Boot architecture and uses DTOs + MapStruct as required.

---

## Tech Stack
- **Java 21**
- **Spring Boot**
- **Spring Security (Basic Authentication)**
- **Spring Data JPA + Hibernate**
- **PostgreSQL**
- **Lombok**
- **Liquibase** (database migrations)
- **MapStruct** (Entity ↔ DTO mapping)
- **JUnit 5 + Mockito** 

---

## Features

### Security (Required)
- User registration
- Authentication (Basic Auth)
- Change password
- Edit profile
- Admin creates users
- Admin blocks/unblocks users
- Role-based access control (ADMIN / TEACHER / STUDENT)

### Core Functionality
- CRUD for courses
- Lessons for each course
- Student enrollments
- Reviews (students can update/delete only their own reviews)

---

## Roles and Access

| Role        | Permissions |
|------------|-------------|
| **ADMIN**   | View users, create users, change roles, block/unblock users |
| **TEACHER** | Create and manage courses, create lessons |
| **STUDENT** | Enroll in courses, create/update/delete own reviews, view courses and lessons |

---

## Database and Migrations (Liquibase)
The database schema is created automatically using **Liquibase** migrations.

Liquibase changelog files are located in:
src/main/resources/db/changelog/


Migrations include:
- Creating tables
- Inserting initial roles
- Inserting the default admin user (if included in migrations)

---

## How to Run the Project (Local Setup)

### 1) Requirements
- Java 21
- PostgreSQL

### 2) Create Database
Create a PostgreSQL database (example):
```sql
CREATE DATABASE final_bd;
```
Update your database credentials in:
src/main/resources/application.properties


The application starts on:
http://localhost:8081


API Endpoints (Main)
Auth

POST /api/auth/register - register student

POST /api/auth/create-teacher - create teacher (admin only)

GET /api/auth/me - current user info

POST /api/auth/change-password - change password

GET /api/auth/ping - server test

Admin

GET /api/admin/users - list all users

POST /api/admin/users - create user

PUT /api/admin/users/{id}/role - change role

PATCH /api/admin/users/{id}/block - block user

PATCH /api/admin/users/{id}/unblock - unblock user

Courses

POST /api/courses - create course

GET /api/courses - get all courses

GET /api/courses/{id} - get course by id

PUT /api/courses/{id} - update course

DELETE /api/courses/{id} - delete course

Lessons

POST /api/lessons - create lesson

GET /api/lessons/course/{courseId} - get lessons by course

PUT /api/lessons/{lessonId} - update lesson

DELETE /api/lessons/{lessonId} - delete lesson

Enrollments

POST /api/enrollments - enroll in a course

GET /api/enrollments/my - get current student's enrollments

GET /api/enrollments/course/{courseId} - get enrollments by course

DELETE /api/enrollments/course/{courseId} - cancel enrollment

Reviews

POST /api/reviews - create review (only enrolled students)

GET /api/reviews/course/{courseId} - get reviews by course

PUT /api/reviews/{reviewId} - update review (only author)

DELETE /api/reviews/{reviewId} - delete review (only author)

DTO and MapStruct (Required)

This project does not return Entity objects from REST controllers.
All API responses use Response DTOs, and all requests use Request DTOs.

Entity ↔ DTO mapping is implemented using MapStruct.

Project Architecture (By Layers)

Entity - database models

DTO - Request / Response models

Mapper - MapStruct mappers

Repository - database access only

Service - business logic only

Controller - REST API layer (no business logic)

Postman collection is included in the repository:

postman/finalrpo.postman_collection.json

Unit Testing (Required)

Each service will be covered with unit tests using:

JUnit 5

Mockito

Tests will check:

saving data

retrieving data

business logic scenarios (role checks, ownership checks, validation logic)

Author

Student: Mergen Tlegen