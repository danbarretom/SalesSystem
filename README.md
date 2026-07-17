# SalesSystem

A Spring Boot REST API for managing products, customers, and sales — the current form of a project that
started as a pure Java SE console application for a college OOP course and was rebuilt, from the ground up,
into a tested, database-backed backend.

🔗 **Live demo:** [`sistema-vendas-e125.onrender.com/swagger-ui.html`](https://sistema-vendas-e125.onrender.com/swagger-ui.html)
(free-tier hosting — the first request after a period of inactivity can take 30-60s to wake up).

## 📖 Project History

This repository is kept as a single git history on purpose — the goal is to keep the whole learning process
visible, not just the latest state:

* **[`v1.0.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.0.0)** — Original submission for
  an Object-Oriented Programming college assignment: a console app managing products, customers, and sales
  through flat `.txt` files, with CRUD operations and a classic Manager/Model class split. Graded 10/10.
* **[`v1.2.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.2.0)** — The same Java SE app,
  refined independently after the grade was in: a Clean Code pass across every manager, a full JUnit 5 suite
  (100% class coverage), custom exceptions, and a GitHub Actions CI/CD pipeline.
* **[`v2.0.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v2.0.0)** *(this version)* — A complete
  architectural rewrite into a Spring Boot REST API: a real relational database instead of flat files, a
  layered architecture (controller/service/repository/DTO), Bean Validation, centralized exception handling,
  optimistic locking, an automated test suite, and a live Docker deployment. The Java SE version stays fully
  intact and browsable at the tags above — nothing was thrown away, just outgrown.

A frontend is the planned next chapter, kept as its own milestone rather than bundled in here — see
[Roadmap](#-roadmap) below.

## 🚀 Features

**Domain Operations**
* **Product management** — full CRUD, with optimistic locking (`@Version`) to prevent lost updates under
  concurrent stock changes, plus a dedicated low-stock query.
* **Customer management** — full CRUD for customers eligible for credit sales.
* **Sales** — cash (À Vista) and credit (A Prazo) sales, with stock validation/deduction, automatic
  subtotal/total calculation, credit-sale rules (a valid customer and due date are required), and queries by
  date range.

**Architecture & Quality**
* **Layered architecture** — controllers stay thin, business rules live in the service layer, and entities
  are never exposed directly: dedicated request/response DTOs carry Bean Validation everywhere.
* **Centralized error handling** — a single `@RestControllerAdvice` maps domain exceptions, validation
  failures, malformed requests, and concurrency conflicts to consistent, structured JSON error responses
  (`404`/`400`/`409`) instead of leaking stack traces.
* **Automated test suite** — 71 tests across four layers: Mockito unit tests for business rules, `@DataJpaTest`
  for custom queries, `@WebMvcTest` for HTTP-layer behavior, and a full-context integration test proving that
  Hibernate's dirty-checking actually persists stock changes, not just an in-memory mutation.
* **CI/CD** — GitHub Actions runs the full test suite on every push/PR to `main`/`dev`; merging a `release/*`
  branch into `main` auto-creates the GitHub tag and Release, reading the version straight from
  `CHANGELOG.md`. Deployed as a Docker container on Render, built from a multi-stage `Dockerfile`.

## 🛠️ Tech Stack

* **Framework:** Spring Boot 4.1, Spring Data JPA, Spring Web MVC
* **Language:** Java 21 (Eclipse Temurin LTS)
* **Database:** PostgreSQL (hosted on Supabase) for the running app; H2 in-memory for the test suite
* **Testing:** JUnit 5, Mockito, AssertJ
* **API Docs:** springdoc-openapi (Swagger UI)
* **Build/DevOps:** Maven, Docker, GitHub Actions, GitFlow (Semantic Versioning)
* **Deployment:** Render (Docker runtime)

## 🔌 API Overview

| Method   | Endpoint                            | Description                        |
|----------|--------------------------------------|-------------------------------------|
| `POST`   | `/api/clientes`                     | Register a customer                |
| `GET`    | `/api/clientes`                     | List customers                     |
| `GET`    | `/api/clientes/{id}`                | Get a customer by id                |
| `PUT`    | `/api/clientes/{id}`                | Update a customer                   |
| `DELETE` | `/api/clientes/{id}`                | Delete a customer                   |
| `POST`   | `/api/produtos`                     | Register a product                  |
| `GET`    | `/api/produtos`                     | List products                       |
| `GET`    | `/api/produtos/estoque-baixo`       | List products below minimum stock   |
| `GET`    | `/api/produtos/{id}`                | Get a product by id                  |
| `PUT`    | `/api/produtos/{id}`                | Update a product                     |
| `DELETE` | `/api/produtos/{id}`                | Delete a product                     |
| `POST`   | `/api/vendas`                       | Register a sale                      |
| `GET`    | `/api/vendas`                       | List sales                           |
| `GET`    | `/api/vendas/periodo?inicio=&fim=`  | List sales in a date range           |

Full interactive docs (request/response schemas, try-it-out) are served by Swagger UI once the app is
running — see below.

## 💻 Running the Application

Requires JDK 21 and a PostgreSQL database.

```bash
git clone https://github.com/danbarretom/SalesSystem.git
cd SalesSystem
```

By default, `application.properties` points at the maintainer's own Supabase project, which needs a password
you won't have. Point it at your own PostgreSQL instance instead by exporting these before running — Spring
Boot's environment variables always take priority over `application.properties`:

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://<host>:<port>/<database>"
export SPRING_DATASOURCE_USERNAME="<user>"
export SPRING_DATASOURCE_PASSWORD="<password>"

./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`; Swagger UI is available at
`http://localhost:8080/swagger-ui.html`.

Run the test suite (uses an in-memory H2 database automatically — no setup needed):

```bash
./mvnw test
```

## 🔮 Roadmap

* **Frontend** *(planned `v3.0.0`)* — a client application consuming this API, released as its own milestone.
* **Workflow Automation & AI Integration** — automated daily sales reports (email/WhatsApp) and predictive AI
  models over sales history for inventory alerts.

---
👨‍💻 **Author:** Daniel Farias Barreto de Moura
