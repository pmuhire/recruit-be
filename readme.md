# Recruitment System Backend

A minimalistic recruitment system backend built with **Spring Boot**, **Spring Security**, **Postgresql**,**JWT Authentication**, and **JPA/Hibernate**. Supports role-based access control with **APPLICANT**, **HR**, and **SUPERADMIN** roles.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Getting Started](#getting-started)
4. [Database Setup](#database-setup)
5. [Running the Application](#running-the-application)
6. [API Endpoints](#api-endpoints)
7. [Roles & Permissions](#roles--permissions)
8. [Test Data](#test-data)
9. [Logging & Debugging](#logging--debugging)
10. [Common Issues](#common-issues)

---

## Project Overview

This backend provides functionality for:

- User registration and login with JWT authentication
- Job creation, closing, and listing
- Application management (approve/reject)
- Role-based access control (RBAC)
- Secure endpoints with meaningful JSON responses on unauthorized access

---

## Technology Stack

- **Java 21**
- **Spring Boot 4.x**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Hibernate / JPA**
- **H2 / PSQL** (configurable in `application.properties`)
- **Maven** for dependency management

---
## Base URL

- **Local:** `http://localhost:8000/`
- **Deployed:** `https://recruit-be-production.up.railway.app/`

> For secured endpoints, include the JWT token in the header:
>
> ```http
> Authorization: Bearer <your-jwt-token>
> ```

---

## API Endpoints

### Authentication

| Method | Endpoint           | Access  | Request Body       | Response           |
|--------|------------------|--------|-----------------|------------------|
| POST   | /api/auth/register | Public | RegisterRequest  | AuthResponse (JWT token) |
| POST   | /api/auth/login    | Public | LoginRequest     | AuthResponse (JWT token) |

**RegisterRequest Example**

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

| Method | Endpoint             | Role/Access         | Request Body | Response            |
| ------ | -------------------- | ------------------- | ------------ | ------------------- |
| POST   | /api/jobs/create     | HR, SUPERADMIN      | JobRequest   | JobResponse         |
| GET    | /api/jobs            | All logged-in users | None         | List of JobResponse |
| POST   | /api/jobs/{id}/close | HR, SUPERADMIN      | None         | Success message     |

| Method | Endpoint                       | Role/Access    | Request Body | Response        |
| ------ | ------------------------------ | -------------- | ------------ | --------------- |
| POST   | /api/applications/{id}/approve | HR, SUPERADMIN | None         | Success message |
| POST   | /api/applications/{id}/reject  | HR, SUPERADMIN | None         | Success message |

## Getting Started

1. **Clone the repository**

```bash
git clone <repo-url>
cd system