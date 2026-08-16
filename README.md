# TripApp Backend

Backend REST API for TripApp, built with Java and Quarkus.

- **Frontend:** [TripApp Frontend](https://github.com/IanaElis/trip-app-frontend)
- **Project:** [TripApp](https://github.com/IanaElis/trip-app)

## Technologies
- ### Core
  - Java
  - Quarkus
  - Maven
- ### Persistence
  - PostgreSQL
  - Hibernate ORM with Panache
  - Flyway
- ### Backend
  - Hibernate Validator
  - MapStruct
  - Quarkus Mailer
  - Quarkus Scheduler
- ### Testing
  - JUnit 5
  - Mockito

## Architecture
The backend follows a **modular monolith** architecture. The application
is deployed as a single unit, while its functionality is divided into
independent business modules.

### Modules

- **User** – registration, authentication, authorization, profile management,
  password recovery, and account administration.
- **Travel** – trip, itinerary, and location management.
  - Itinerary - submodule focused on itinerary management.
  - Location - submodule for location management.
- **Notifications** – notification generation and delivery.

The modules are organized into three technical layers:

- **Presentation** – REST controllers responsible for handling HTTP requests
  and responses.
- **Business logic** – services implementing application rules and coordinating
  operations.
- **Persistence** – repositories responsible for database access.

Communication between layers is performed through defined interfaces,
with DTOs used to separate the API model from persistence entities

See the [project repository](https://github.com/IanaElis/trip-app) for the complete system architecture and database model.

## Project structure
```
src/main/java/
├── user/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   └── mapper/
│
├── travel/
│   ├── itinerary/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   └── mapper/
│   │
│   └── location/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       └── mapper/
│
├── notifications/
├── service/
├── repository/
├── entity/
├── dto/
└── sender/
```

## API

The backend exposes a RESTful API using JSON for communication with the
React frontend.

| Resource               | Description                           |
| ---------------------- |---------------------------------------|
| `/auth`                | Authentication and account management |
| `/admin`               | Administrative user management        |
| `/trips`               | Trip management                       |
| `/trips/{trip_id}/...` | Itinerary item management             |

## Authentication & Security

TripApp uses JWT-based authentication with short-lived access tokens and refresh tokens.

- Access and refresh tokens are stored in **HTTP-only cookies** to prevent client-side JavaScript from directly accessing them.
- Access tokens expire after 15 minutes, refresh tokens - after 14 days. 
- Passwords are hashed before being stored in the database.
- Authentication and authorization are enforced on protected endpoints.
- Role-based access control distinguishes regular users from administrators.
- Refresh tokens are validated before token rotation (issuing new token pair).
- Repeated failed login attempts are rate-limited to reduce the risk of brute-force attacks. (10 failed attempts in 10 minutes)
- Password reset tokens are securely generated, hashed before storage, and expire after 15 minutes.
- User accounts can be disabled by administrators, preventing disabled users from accessing the application.
- Sensitive configuration values, such as database credentials and API keys, are provided through environment variables rather than stored in the repository.

## Database
TripApp uses **PostgreSQL** for persistent storage.

Database schema changes are managed through **Flyway migrations**, which are
applied when the application starts.

The complete physical database model is available in the
[main TripApp repository](https://github.com/IanaElis/trip-app).

## Testing
The backend is tested using **JUnit 5** and **Mockito**.

Tests cover the main business logic and service-layer functionality, including:
- Trip management;
- Itinerary item creation and modification;
- Accommodation, flight, transport, and activity handling;
- Validation and error handling;
- Authentication-related functionality.

**JaCoCo** is used to measure code coverage.

## Configuration
The application requires the following environment variables:

| Variable                 | Description                              | Default  |
|--------------------------|------------------------------------------|----------|
| DB_USERNAME              | PostgreSQL username                      | postgres |
| DB_PASSWORD              | PostgreSQL password                      | trip     |
| DB_NAME                  | PostgreSQL database name                 | tripApp  |
| GMAIL_EMAIL              | Email address used to send notifications | -        |
| GMAIL_PASSWORD           | Email account password                   | -        |

> **Note:** Do not commit credentials, API keys, or other secrets to the repository.
> Use environment variables or a local `.env`/configuration file instead.

## Running locally

### Prerequisites

- Java 21+
- Docker Desktop

###  1. Start PostgreSQL
The PostgreSQL database is provided through Docker Compose:
```bash
docker compose up -d
```
> Database schema migrations are automatically applied using Flyway during application startup.
### 2. Configure environment variables
### 3. Start the backend
```bash
./mvnw quarkus:dev
```

The backend is then available at:
http://localhost:8080

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly
  JAX-RS)
- OpenID Connect Client ([guide](https://quarkus.io/guides/security-openid-connect-client)): Get and refresh access
  tokens from OpenID Connect providers
- Flyway ([guide](https://quarkus.io/guides/flyway)): Handle your database schema migrations
- REST Client - OpenID Connect Filter ([guide](https://quarkus.io/guides/security-openid-connect-client)): Use REST
  Client filter to get and refresh access tokens with OpenId Connect Client and send them as HTTP Authorization Bearer
  tokens
- Scheduler ([guide](https://quarkus.io/guides/scheduler)): Schedule recurring tasks and periodic jobs using cron
  expressions or fixed intervals
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- REST Client - OpenID Connect Token Propagation ([guide](https://quarkus.io/guides/security-openid-connect-client)):
  Use REST Client to propagate the incoming Bearer access token or token acquired from Authorization Code Flow as HTTP
  Authorization Bearer token
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Bean validation using Hibernate Validator and
  Jakarta Validation annotations
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus
  REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplified JPA/Hibernate data
  access layer with active record and repository patterns
- Mailer ([guide](https://quarkus.io/guides/mailer)): Send emails
- SmallRye JWT Build ([guide](https://quarkus.io/guides/security-jwt-build)): Create JSON Web Token with SmallRye JWT
  Build API
