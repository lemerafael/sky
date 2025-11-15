# SKY Projects Portal
TODO: Add brief description

## APIs

### Users
TODO

### Projects
TODO

### Auth
TODO

## Security
The project features a token-based authentication.

The *SecurityConfig* class allows public acess for the Swagger endpoints and for the endpoints located in /public and in /auth. The other endpoints from /users and from /projects are accessible only with authentication.

The *OpenApiConfig* class is responsible for adding the Authorization feature in the Swagger webpage.

The classes from the security.jwt package are responsible for generating the token and later verifying it.

The *AuthenticationService* has a login endpoint for generating a token. The current implementation does not verify the e-mail or the password provided for login. 

For future implementations, it is desirable to integrate the user information with the information previously stored in the database. It is also desirable to apply password hashing before storing it on the database and also verify user and password from the stored user data.

## How to run the project?

1. Compile the java project
```bash
$ ./mvnw clean package -DskipTests
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