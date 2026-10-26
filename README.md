# Spring Boot RBAC JWT

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-rbac-jwt/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-rbac-jwt/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen?logo=springboot)
[![Dependabot](https://img.shields.io/badge/Dependabot-enabled-blue?logo=dependabot)](.github/dependabot.yml)

A Spring Boot application demonstrating Role-Based Access Control (RBAC) with JSON Web Token (JWT) authentication.

## Table of Contents

- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [API Documentation](#api-documentation)
- [Authentication Flow](#authentication-flow)
- [Testing the API](#testing-the-api)
- [Running the Tests](#running-the-tests)
- [Continuous Integration](#continuous-integration)
- [Dependency Updates](#dependency-updates)

## Overview

This project is a RESTful API built with Spring Boot that implements:

- JWT-based authentication
- Role-based access control (RBAC)
- User management with different roles (USER, ADMIN)
- Secure endpoints based on user roles

## Technologies Used

- Java 25
- Spring Boot 4.1
- Spring Security 7
- JWT (JSON Web Token) via [JJWT](https://github.com/jwtk/jjwt) 0.13
- JPA / Hibernate
- MySQL (H2 for tests)
- Lombok
- Maven (via the bundled Maven Wrapper)

## Setup

### Prerequisites

- JDK 25
- MySQL 8+
- Maven is optional: use the bundled wrapper (`./mvnw`)

### Database Setup

1. Create a MySQL database named `jwt`

```sql
CREATE
DATABASE jwt;
```

2. Update the database configuration in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jwt
spring.datasource.username=yu71
spring.datasource.password=53cret
```

3. Set a strong JWT signing key. The token is signed with HMAC-SHA, which requires a key of **at least 32 bytes
   (256 bits)**; shorter keys are rejected by JJWT with a `WeakKeyException`:

```properties
jwt.signing.key=change-me-to-a-secret-of-at-least-32-bytes
jwt.token.validity=18000
```

### Building and Running the Application

1. Clone the repository

```bash
git clone https://github.com/hendisantika/spring-boot-rbac-jwt.git
cd spring-boot-rbac-jwt
```

2. Build the application

```bash
./mvnw clean install
```

3. Run the application

```bash
./mvnw spring-boot:run
```

or

```bash
java -jar target/rbac-jwt-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080.

## API Documentation

### Authentication Endpoints

#### User Registration

```
POST /users/register
```

Request body:

```json
{
   "username": "user1",
   "password": "password",
   "email": "user1@example.com",
   "phone": "1234567890",
   "name": "User One"
}
```

Response: User object

#### User Login

```
POST /users/authenticate
```

Request body:

```json
{
   "username": "user1",
   "password": "password"
}
```

Response:

```json
{
   "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### User Endpoints

#### User Ping (Requires USER role)

```
GET /users/userping
```

Response:

```
Any User Can Read This
```

#### Admin Ping (Requires ADMIN role)

```
GET /users/adminping
```

Response:

```
Only Admins Can Read This
```

#### Create Employee (Requires ADMIN role)

```
POST /users/create/employee
```

Request body:

```json
{
   "username": "employee1",
   "password": "password",
   "email": "employee1@example.com",
   "phone": "1234567890",
   "name": "Employee One"
}
```

Response: User object

#### Find All Users (Requires ADMIN role)

```
GET /users/find/all
```

Response: Array of User objects

#### Find User by Username (Requires ADMIN role)

```
GET /users/find/by/username?username=user1
```

Response: User object

## Authentication Flow

1. **Registration**: User registers with username, password, and other details
2. **Authentication**: User logs in with username and password
3. **Token Generation**: Server validates credentials and generates a JWT token
4. **Authorization**: User includes the token in the Authorization header for subsequent requests
5. **Access Control**: Server validates the token and checks user roles for access to protected endpoints

## Testing the API

You can test the API using tools like Postman, cURL, or any HTTP client.

### Example with cURL

1. Register a new user:

```bash
curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password",
    "email": "testuser@example.com",
    "phone": "1234567890",
    "name": "Test User"
  }'
```

2. Authenticate and get token:

```bash
curl -X POST http://localhost:8080/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password"
  }'
```

3. Access a protected endpoint with the token:

```bash
curl -X GET http://localhost:8080/users/userping \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Example with Postman

1. Create a new request for user registration:
   - Method: POST
   - URL: http://localhost:8080/users/register
   - Body (raw, JSON):
   ```json
   {
     "username": "testuser",
     "password": "password",
     "email": "testuser@example.com",
     "phone": "1234567890",
     "name": "Test User"
   }
   ```

2. Create a new request for authentication:
   - Method: POST
   - URL: http://localhost:8080/users/authenticate
   - Body (raw, JSON):
   ```json
   {
     "username": "testuser",
     "password": "password"
   }
   ```
   - Save the token from the response

3. Create a new request for accessing a protected endpoint:
   - Method: GET
   - URL: http://localhost:8080/users/userping
   - Headers:
      - Key: Authorization
      - Value: Bearer YOUR_TOKEN_HERE

## Running the Tests

The test suite uses an in-memory H2 database, so no MySQL instance is needed:

```bash
./mvnw test
```

## Continuous Integration

GitHub Actions ([`.github/workflows/maven.yml`](.github/workflows/maven.yml)) runs on every push and pull request
to `main` (and can be triggered manually):

- **build**: sets up Temurin JDK 25 with Maven caching and runs `./mvnw -B package` (compile + tests)
- **dependency-graph**: on pushes to `main` only, submits the Maven dependency graph to GitHub to improve
  Dependabot security alerts

The workflow uses read-only permissions by default and cancels superseded runs on the same branch.

## Dependency Updates

[Dependabot](.github/dependabot.yml) checks daily at 05:00 (Asia/Jakarta) for:

| Ecosystem      | Grouping                                                                                  | Commit prefix                    |
|----------------|-------------------------------------------------------------------------------------------|----------------------------------|
| Maven          | `spring-boot`, `jjwt`, `maven-wrapper`, plus one group for all other minor/patch updates | `build(deps)` / `build(deps-dev)` |
| GitHub Actions | all actions in a single PR                                                                | `ci(deps)`                       |

Pull requests are labelled `dependencies` plus `java` or `github_actions`.

## License

This project is licensed under the MIT License.
