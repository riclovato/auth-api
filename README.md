# auth-api

> API for authentication built with Java and Spring Boot using JWT and role-based access control. Intended as a portfolio project for internship applications.

---

## Overview

**Purpose:** provide endpoints for user signup, login, token refresh, logout and role-based authorization.

**Core features:** user registration, JWT access tokens, refresh tokens with revocation, role checks, input validation and centralized error handling.

---

## Technologies

| Layer       | Tool                              |
|-------------|-----------------------------------|
| Language    | Java                              |
| Framework   | Spring Boot                       |
| Security    | Spring Security; JWT              |
| Persistence | JPA / Hibernate                   |
| Database    | PostgreSQL (recommended); H2 for tests |
| Tests       | JUnit; Mockito                    |
| Build       | Maven                             |
| Container   | Docker                            |
| Migrations  | Flyway (recommended)              |

---

## Requirements

- Java 17 or newer
- Maven 3.6 or newer
- Docker (optional)
- PostgreSQL for production; H2 can be used for local development and tests

---

## Environment Variables

Create a `.env.example` and copy to `.env`, or set variables in your environment:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/authdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
JWT_SECRET=replace_with_a_strong_secret
JWT_EXPIRATION_MS=3600000
REFRESH_TOKEN_EXPIRATION_MS=2592000000
```

> ⚠️ Do not commit secrets to the repository.

---

## Run Locally

1. **Clone the repository**

```bash
git clone https://github.com/riclovato/auth-api.git
cd auth-api
```

2. **Configure environment variables** as shown above.

3. **Run with Maven**

```bash
mvn clean spring-boot:run
```

4. **API base URL**

```
http://localhost:8080
```

---

## Docker

**Build image**

```bash
docker build -t auth-api:latest .
```

**Example `docker-compose.yml`**

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: authdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  app:
    image: auth-api:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/authdb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      JWT_SECRET: replace_with_a_strong_secret
    ports:
      - "8080:8080"
    depends_on:
      - db
```

---

## API Endpoints

Base path: `/api`

| Method | Route          | Auth             | Description                                      |
|--------|----------------|------------------|--------------------------------------------------|
| POST   | /auth/signup   | No               | Register a new user                              |
| POST   | /auth/login    | No               | Authenticate and return access + refresh tokens  |
| POST   | /auth/refresh  | No               | Exchange refresh token for new access token      |
| POST   | /auth/logout   | Yes              | Revoke refresh token                             |
| GET    | /users         | Yes (ROLE_ADMIN) | List users (example protected endpoint)          |

### Signup

```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "ricardo",
  "email": "ricardo@example.com",
  "password": "StrongPassword123"
}
```

### Login Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
  "refreshToken": "d1f2e3a4-5b6c-7d8e-9f01-23456789abcd",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

### Authenticated Requests

```
Authorization: Bearer <accessToken>
```

---

## Tests

```bash
mvn test
```

> Recommended: use Testcontainers or H2 for integration tests. Aim for at least 70% coverage on service and controller layers.

---

## Security Notes

- Passwords must be stored with **BCrypt** hashing.
- Use short-lived JWT access tokens and persisted refresh tokens with revocation support.
- Validate all inputs with **Bean Validation**.
- Implement global exception handling with `@ControllerAdvice`.
- Add **rate limiting** for authentication endpoints (e.g., Bucket4j) to reduce brute-force risk.
- Keep secrets out of the repository — use environment variables or a secrets manager.

---

## Observability & Documentation

- Add **Spring Actuator** for health checks and metrics.
- Provide **OpenAPI/Swagger** documentation for all endpoints.
- Use structured logging with **SLF4J** and **Logback**.

---

## CI/CD Suggestions

GitHub Actions pipeline:

1. Build and run tests: `mvn clean verify`
2. Static analysis: SpotBugs, Checkstyle
3. Build Docker image and push to registry
4. _(Optional)_ Deploy to staging environment

---

## Project Structure

```
src/main/java         # Application source code
src/main/resources    # Configuration and migrations
src/test              # Unit and integration tests
Dockerfile            # Container image build
docker-compose.yml    # Local environment example
```

---

## Portfolio Highlights

When presenting this project, emphasize:

- Complete authentication flow: signup, login, refresh, logout.
- Refresh token persistence and revocation logic.
- Unit and integration tests covering security logic.
- OpenAPI documentation and Postman/Insomnia collection.
- Dockerized setup and CI pipeline.

---

## Contribution

Contributions are welcome. Follow these rules:

- Create a feature branch named `feature/<short-description>`.
- Keep commits small and descriptive.
- Add tests for new functionality.
- Update README and API docs when behavior changes.

---

## License

This project is available under the [MIT License](LICENSE).
