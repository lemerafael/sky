# SKY Projects Portal

The Projects Portal is a Spring Boot REST API for managing user accounts and their assigned external projects. The application demonstrates problem-solving abilities and software engineering best practices including secure authentication, clean architecture, and comprehensive testing.

Functionality: Users can authenticate via JWT tokens, manage their profile, and track assigned projects through RESTful endpoints. All data is persisted in PostgreSQL and the entire stack is containerized with Docker for local development.

Technical Foundation: Built with Java and the Spring Framework ecosystem, the project showcases adherence to Spring best practices including separation of concerns, dependency injection, security configuration (BCrypt password hashing, externalized configuration), and comprehensive unit testing with JUnit 5.

Goal: This project serves as an assessment of problem-solving abilities alongside soft skills including code organization, attention to architectural detail, and commitment to best practices in enterprise Java development.

## Endpoints

### User Management

| Endpoint                | Method | Description                   |
|-------------------------|--------|-------------------------------|
| `/v1/users`             | GET    | Get all users                 |
| `/v1/users/{id}`        | GET    | Get user by id                |
| `/v1/users`             | POST   | Create new user               |
| `/v1/users/{id}`        | PUT    | Update user by id             |
| `/v1/users/{id}`        | DELETE | Delete user by id             |

### Projects

| Endpoint                | Method | Description                   |
|-------------------------|--------|-------------------------------|
| `/v1/projects/{id}`     | GET    | Get projects from user by id  |
| `/v1/projects/{id}`     | POST   | Add new project to user by id |

### Auth

| Endpoint                | Method | Description                   |
|-------------------------|--------|-------------------------------|
| `/auth/login`           | POST   | Get login token for user      |

## Security

The project features a token-based authentication.

The *SecurityConfig* class allows public acess for the Swagger endpoints and for the endpoints located in /public and in /auth. The other endpoints from /users and from /projects are accessible only with authentication.

The *OpenApiConfig* class is responsible for adding the Authorization feature in the Swagger webpage.

The classes from the security.jwt package are responsible for generating the token and later verifying it.

The *AuthenticationService* has a login endpoint for generating a token. The current implementation verifies the e-mail and the password provided (after hashing) for login with the stored user data.

## Unit testing

A testing suite was designed with JUnit 5 to cover basic scenario and edge cases. The tests are executed by maven, after the Java compilation.

The suite covers the cases for the users endpoint. The tests for projects and authorization endpoints are pending.

## Running Instructions

1. Compile the java project
```bash
$ ./mvnw clean package
```
2. Copy the jar file to src/main/docker
```bash
$  cp .\target\projects-0.0.1-SNAPSHOT.jar .\src\main\docker\
```
3. Go to folder src/main/docker, build and run the docker-compose
```bash
$ cd src/main/docker
$ docker-compose up --build
```
4. When the test is finished, close the application with Ctrl+C and stop the containers
```bash
$ docker-compose down
```

## Manual testing

- Access the Swagger endpoint on http://localhost:8080/swagger-ui/index.html
- Get the authorization token using the `/auth/login` endpoint. Use the predefined demo info email `admin@email.com` and password `admin`.
- Click in the `Authorize` button and add the generated token. All endpoints should be available for testing.

## TODOs

## External projects
- Separate service and controller data
- Implement error treating

## Testing for Users
- Implement create tests with missing data
- Implement create tests with extra data
- Implement update tests
- Implement update tests with missing data
- Implement update tests with extra data
- Implement delete tests
- Implement delete tests with invalid info from user
- Implement delete tests with info from nonexsistent user

## Testing for Projects
- Implement create tests
- Implement create tests with invalid project info
- Implement create tests with invalid info from user
- Implement create tests with info from nonexsistent user
- Implement get tests
- Implement get tests with invalid info from user
- Implement get tests with info from nonexsistent user

## Testing for Authentication
- Implement login tests
- Implement login tests with invalid info from user
- Implement login tests with missing info from user
- Implement login tests with extra info from user

## Future implementations

### Role based authorization
- Add role based authorization: ADMIN, MANAGER and USER. Add this information to tb_users.
- ADMIN can create user and projects.
- MANAGER can create projects. It can only access its own projects. It can only access information from the users related to its own projects.
- USER cannot create new user or project. It can only acess its own projects and its own user information.

### Custom Exceptions
- Implement some custom Exceptions like UserNotFound or ProjectNotFound