# Reliant Platform

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE.md)

## Overview

A Spring Boot REST API, built with Domain-Driven Design and CQRS, that backs the **Reliant** web application: monitoring and traceability of HVOF (High Velocity Oxygen Fuel) thermal-spray recuperation processes. It records HVOF systems and their process recipes, traces recovered components and recuperation orders, ingests process readings from the spray booth and classifies them against the recipe bands, opens fault cases from out-of-band readings, and issues quality certificates and configurable reports.

The project is organized around bounded contexts plus a `shared` kernel, using application-layer command/query contracts, a `Result` type for command outcomes, and JPA persistence on MySQL.

| Bounded context | Package | Responsibility |
|---|---|---|
| Equipment | `equipment` | HVOF systems, subsystems, controllers, process recipes, edge devices |
| IAM | `iam` | Organizations, users, roles, JWT authentication |
| Traceability | `traceability` | Customers, recovered components, recuperation orders |
| Process Monitoring | `processmonitoring` | Spray sessions, passes, readings and band classification |
| Fault Diagnosis | `faultdiagnosis` | Cause-effect rules and fault cases |
| Notifications | `notifications` | In-app notifications and email alerts |
| Reporting | `reporting` | Report templates, user preferences, quality certificates |
| Shared | `shared` | Auditable base classes, `Result`, error handling, OpenAPI, CORS |

> Contexts are added incrementally; see the Features section for what is available in the current release.

## Features
All endpoints live under `/api/v1` and, except sign-up and sign-in, require a `Bearer` JWT. The authenticated user's organization is the tenant boundary.

| Bounded context | Endpoints |
|---|---|
| IAM | `POST /users` (sign-up: organization + administrator), `POST /tokens` (sign-in), `GET /users`, `GET /users/me`, `GET /users/{id}`, `PUT /users/{id}/roles`, `GET /roles` |
| Equipment | `POST/GET /hvof-systems`, `GET /hvof-systems/{id}`, `POST/GET /hvof-systems/{id}/subsystems`, `POST/GET /hvof-systems/{id}/controllers`, `POST/GET /recipes`, `GET /recipes/{id}`, `PATCH /recipes/{id}/publication` |
| Traceability | `POST/GET/PUT /customers`, `POST/GET /components?customerId=`, `POST/GET /recuperations`, `PATCH /recuperations/{id}/completion` |
| Process Monitoring | `POST/GET /spray-sessions`, `PATCH /spray-sessions/{id}/completion`, `PATCH /spray-sessions/{id}/abortion`, `POST/GET /spray-sessions/{id}/readings`, `GET /spray-sessions/{id}/deviations`, `POST /spray-sessions/{id}/passes` |
| Fault Diagnosis | `GET /fault-rules` (seeds the FESA defaults), `POST/PUT /fault-rules`, `GET /fault-cases?spraySessionId=&status=`, `PATCH /fault-cases/{id}/acknowledgement`, `PATCH /fault-cases/{id}/resolution` |
| Reporting | `GET /report-templates` (seeds the standard templates), `POST/PUT /report-templates`, `PATCH /report-templates/{id}/status`, `POST/GET /generated-reports` |

Out-of-band readings raise a domain event that opens a fault case with the probable cause and recommended action of the matching rule. Interactive API docs: Swagger UI at `/swagger-ui.html`.

## Technologies
- Java 21 (compiles with JDK 21 or newer) and Spring Boot 4
- Apache Maven (or the bundled `./mvnw` wrapper)
- Spring Web (REST controllers)
- Spring Data JPA (Hibernate) with MySQL
- Spring Validation (Jakarta Bean Validation)
- Spring Security with JWT (jjwt)
- SpringDoc OpenAPI 3 / Swagger UI
- Lombok (compile-time helpers)
- PlantUML (architecture diagrams in `docs/`)

## Prerequisites
- JDK 21 or higher
- Maven (or the bundled `./mvnw` wrapper)
- MySQL 8.4+ running locally

## Environment variables
The application resolves the active profile and database credentials from the environment. Set the following before running locally:

| Variable | Description |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile (`dev` locally) |
| `DATABASE_USER` | MySQL username |
| `DATABASE_PASSWORD` | MySQL password |

Production (`SPRING_PROFILES_ACTIVE=prod`) reads `DATABASE_URL`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASSWORD`, `JWT_SECRET` (32+ characters), `CORS_ALLOWED_ORIGINS` and `PORT` (see `application-prod.properties`). The schema is created on startup. Deployment on Azure App Service is described in [`docs/azure-deployment.md`](docs/azure-deployment.md).

## Spring profiles

| Profile | Usage |
|---|---|
| *(default)* | Uses `localhost:3306` with SSL enabled; SQL logging off |
| `dev` | Uses `localhost:3306` with SSL disabled; SQL logging on; creates the `reliant-os` database if missing |
| `prod` | Builds the JDBC URL from env vars; SSL required; schema updated on startup |

## Architecture notes
- `<context>.application.commandservices` and `<context>.application.queryservices` define the application-layer service contracts
- `<context>.application.internal.commandservices` and `<context>.application.internal.queryservices` contain the implementations
- `shared.application.result.Result` models command outcomes without introducing HTTP or domain concepts
- Each context exposes a sealed `*CommandFailure` that the REST layer maps to localized `ProblemDetail` responses (English and Spanish)
- `<context>.interfaces.acl` exposes a context facade; other contexts call it only through an `outboundservices.acl.External*Service`
- The authenticated user's organization (from the JWT) is the tenant boundary for every query and command

## Error responses
- Validation and domain input errors return `400 Bad Request` with a `ProblemDetail` payload.
- Duplicate registrations (for example, an HVOF system code already used by the organization) return `409 Conflict` with a localized `ProblemDetail` payload.
- Not-found lookups return `404 Not Found` with a localized `ProblemDetail` payload.
- Requests outside the caller's organization return `403 Forbidden`.

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/upc-pre-202620-1asi0729-7753-innovacorp/reliant-platform.git
   cd reliant-platform
   ```
2. Set the `SPRING_PROFILES_ACTIVE`, `DATABASE_USER`, and `DATABASE_PASSWORD` environment variables.
3. Build the project:
   ```bash
   ./mvnw clean install
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Access the API:
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - Or send requests directly, for example:
      ```bash
      curl -X POST http://localhost:8080/api/v1/hvof-systems \
           -H "Content-Type: application/json" \
           -d '{"organizationId": 1, "code": "HVOF-01", "serialNumber": "JP5000-2019-0421", "systemManufacturer": "Praxair", "systemModel": "JP-5000", "fuelType": "KEROSENE"}'
      ```

## Web application
The Angular client that consumes this API lives in [`reliant-webapp`](https://github.com/upc-pre-202620-1asi0729-7753-innovacorp/reliant-webapp).

## License
MIT, see [`LICENSE.md`](LICENSE.md).

---
Developed by **InnovaCorp** · UPC · 1ASI0729 Open Source Applications Development.