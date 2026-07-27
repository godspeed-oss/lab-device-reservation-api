\# Lab Device Reservation System



A full-stack laboratory device reservation system built with Spring Boot, MyBatis, MySQL, and a lightweight frontend page.



This project started as a Java console application and was gradually upgraded into a Spring Boot REST API system with authentication, role-based access control, automated tests, Docker configuration, and GitHub Actions support.



\## Features



\### Device Management



\- Search devices by keyword and status

\- View device details

\- Add new devices

\- Update device information

\- Delete devices

\- Prevent deleting devices that already have reservations



\### Reservation Management



\- Create reservations

\- Search reservations by device and date

\- View reservation details

\- Delete reservations

\- Prevent reservations for unavailable devices

\- Prevent reservation time conflicts

\- Restrict normal users to their own reservation records



\### Authentication And Authorization



\- User login

\- BCrypt password verification

\- JWT-style token generation and verification

\- Token-based request authentication

\- Role-based access control

\- Admin users can manage devices

\- Normal users can only manage their own reservations



\### Frontend Page



\- Login page

\- Device list

\- Device search

\- Device add, edit, and delete

\- Reservation list

\- Reservation creation and deletion

\- Role-based button display



\### Engineering Support



\- Layered backend architecture

\- Unified API response format

\- Global exception handling

\- Request parameter validation

\- Swagger / OpenAPI documentation

\- Unit tests with JUnit and Mockito

\- Dockerfile

\- Docker Compose

\- GitHub Actions workflow



\## Tech Stack



\- Java 17

\- Spring Boot

\- Spring Web

\- MyBatis

\- MySQL

\- Maven

\- Lombok

\- Jakarta Validation

\- BCrypt

\- JWT-style token

\- Springdoc OpenAPI

\- JUnit 5

\- Mockito

\- Docker

\- GitHub Actions

\- HTML / CSS / JavaScript



\## Project Structure



```text

src

├── main

│   ├── java

│   │   └── com.lab.reservation

│   │       ├── common

│   │       ├── config

│   │       ├── controller

│   │       ├── dto

│   │       ├── entity

│   │       ├── exception

│   │       ├── interceptor

│   │       ├── mapper

│   │       ├── service

│   │       └── util

│   └── resources

│       ├── application.properties

│       └── static

│           ├── index.html

│           ├── app.js

│           └── styles.css

├── test

│   └── java

│       └── com.lab.reservation

docker

└── mysql

&#x20;   └── init.sql

.github

└── workflows

&#x20;   └── maven-test.yml

```



\## Architecture



```text

Frontend Page

&#x20;   ↓

REST Controller

&#x20;   ↓

Auth Interceptor

&#x20;   ↓

Service Layer

&#x20;   ↓

MyBatis Mapper

&#x20;   ↓

MySQL

```



The project uses a layered architecture:



\- `controller`: Handles HTTP requests

\- `service`: Contains business logic

\- `mapper`: Accesses the database through MyBatis

\- `entity`: Represents database objects

\- `dto`: Defines request and response objects

\- `exception`: Handles business and system exceptions

\- `interceptor`: Checks authentication token

\- `util`: Provides token and password utilities



\## Database Design



\### user



| Field | Type | Description |

|---|---|---|

| id | int | Primary key |

| username | varchar | Login username |

| password | varchar | BCrypt encrypted password |

| role | varchar | User role |



Supported roles:



```text

ADMIN

USER

```



\### device



| Field | Type | Description |

|---|---|---|

| id | int | Primary key |

| name | varchar | Device name |

| type | varchar | Device type |

| status | varchar | Device status |



Supported device status:



```text

Available

Maintenance

Disabled

```



\### reservation



| Field | Type | Description |

|---|---|---|

| id | int | Primary key |

| device\_id | int | Device id |

| user\_id | int | User id |

| user\_name | varchar | Reservation user |

| reservation\_date | date | Reservation date |

| start\_time | time | Start time |

| end\_time | time | End time |



\## API Response Format



All APIs use a unified response format.



Success example:



```json

{

&#x20; "code": 200,

&#x20; "message": "success",

&#x20; "data": {}

}

```



Error example:



```json

{

&#x20; "code": 400,

&#x20; "message": "Device not found",

&#x20; "data": null

}

```



\## Main APIs



\### Auth APIs



| Method | Path | Description |

|---|---|---|

| POST | `/auth/login` | User login |



\### Device APIs



| Method | Path | Description | Permission |

|---|---|---|---|

| GET | `/devices` | Search devices | Login required |

| GET | `/devices/{id}` | Get device by id | Login required |

| POST | `/devices` | Create device | ADMIN |

| PUT | `/devices/{id}` | Update device | ADMIN |

| DELETE | `/devices/{id}` | Delete device | ADMIN |



\### Reservation APIs



| Method | Path | Description | Permission |

|---|---|---|---|

| GET | `/reservations` | Search reservations | Login required |

| GET | `/reservations/{id}` | Get reservation by id | Owner or ADMIN |

| POST | `/reservations` | Create reservation | Login required |

| DELETE | `/reservations/{id}` | Delete reservation | Owner or ADMIN |



\## Authentication Flow



```text

1\. User sends username and password to /auth/login

2\. Server verifies BCrypt encrypted password

3\. Server generates a JWT-style token

4\. Frontend stores token in localStorage

5\. Frontend sends token in Authorization header

6\. AuthInterceptor parses and verifies token

7\. Controller and Service handle business logic based on user role

```



\## Role Rules



```text

ADMIN:

\- Can manage all devices

\- Can view all reservations

\- Can delete all reservations



USER:

\- Can view devices

\- Can create reservations

\- Can view only their own reservations

\- Can delete only their own reservations

```



\## How to Run Locally



\### 1. Create Database



```sql

CREATE DATABASE lab\_reservation\_db;

```



\### 2. Configure Database



Edit:



```text

src/main/resources/application.properties

```



Example:



```properties

spring.datasource.url=jdbc:mysql://localhost:3306/lab\_reservation\_db?useSSL=false\&serverTimezone=Asia/Shanghai\&characterEncoding=utf8\&allowPublicKeyRetrieval=true

spring.datasource.username=root

spring.datasource.password=123456

```



\### 3. Initialize Tables



You can use:



```text

docker/mysql/init.sql

```



Or manually create the tables in MySQL.



\### 4. Run Tests



```powershell

mvn test

```



\### 5. Start Application



```powershell

.\\mvnw.cmd spring-boot:run

```



Open:



```text

http://localhost:8080

```



Swagger UI:



```text

http://localhost:8080/swagger-ui/index.html

```



\## Docker Run



If Docker Desktop is available, run:



```powershell

docker compose up --build

```



Then open:



```text

http://localhost:8080

```



If Docker Hub cannot be accessed due to network restrictions, run the project locally with Maven.



\## Default Accounts



```text

admin / 123456

student / 123456

```



\## Tests



The project includes unit tests for:



\- Device search

\- Device creation

\- Device query

\- Device deletion restriction

\- Reservation creation

\- Reservation time conflict rejection

\- Reservation permission restriction

\- Token generation

\- Token parsing

\- Invalid token handling

\- Tampered token handling



Run:



```powershell

mvn test

```



\## Version History



\### v0.1-api



Initial Spring Boot API version.



\### v0.2-api



Added validation, exception handling, Swagger documentation, and tests.



\### v0.3-auth



Added BCrypt password verification and JWT-style token authentication.



\### v0.4-reservation-permission



Added role-based reservation access control.



\### v0.5-deployment



Added Dockerfile, Docker Compose, database initialization script, and GitHub Actions workflow.



\### v1.0.0



Final learning version with backend API, frontend page, authentication, authorization, tests, documentation, and deployment configuration.



\## Project Value



This project demonstrates the evolution from a basic Java console program to a structured full-stack backend-oriented application.



It covers:



\- Java object-oriented programming

\- Database CRUD

\- REST API design

\- Backend layered architecture

\- Authentication and authorization

\- Request validation

\- Exception handling

\- API documentation

\- Unit testing

\- Frontend and backend interaction

\- Docker-based deployment configuration

\- GitHub-based version management



\## Future Improvements



\- Replace the custom JWT-style token with a mature JWT library

\- Add Spring Security

\- Add refresh token support

\- Add user registration

\- Add reservation approval workflow

\- Add frontend framework such as Vue or React

\- Add Docker image publishing

\- Add production deployment

\- Add more integration tests

