# SalesSystem

A full-stack sales management system — Spring Boot REST API + React/TypeScript frontend — for managing
products, customers, and sales. The current form of a project that started as a pure Java SE console
application for a college OOP course, rebuilt from the ground up first into a tested, database-backed
backend, then into a complete web application.

🔗 **Live demo:** [`sales-system-ivory.vercel.app`](https://sales-system-ivory.vercel.app) (frontend).
API docs: [`sistema-vendas-e125.onrender.com/swagger-ui.html`](https://sistema-vendas-e125.onrender.com/swagger-ui.html)
(backend is free-tier hosting — the first request after a period of inactivity can take 30-60s to wake up).

## 📖 Project History

This repository is kept as a single git history on purpose — the goal is to keep the whole learning process
visible, not just the latest state:

* **[`v1.0.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.0.0)** — Original submission for
  an Object-Oriented Programming college assignment: a console app managing products, customers, and sales
  through flat `.txt` files, with CRUD operations and a classic Manager/Model class split. Graded 10/10.
* **[`v1.2.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.2.0)** — The same Java SE app,
  refined independently after the grade was in: a Clean Code pass across every manager, a full JUnit 5 suite
  (100% class coverage), custom exceptions, and a GitHub Actions CI/CD pipeline.
* **[`v2.0.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v2.0.0)** — A complete architectural
  rewrite into a Spring Boot REST API: a real relational database instead of flat files, a layered
  architecture (controller/service/repository/DTO), Bean Validation, centralized exception handling,
  optimistic locking, an automated test suite, and a live Docker deployment. The Java SE version stays fully
  intact and browsable at the tags above — nothing was thrown away, just outgrown.
* **`v3.0.0`** *(this version)* — A React/TypeScript frontend consuming the REST API: full CRUD for
  Products and Customers, sale registration (cash and credit) with a dashboard summary, deployed
  independently on Vercel. Kept as its own milestone rather than bundled into `v2.0.0`, so the backend-only
  and full-stack states are both preserved in history.

## 🚀 Features

**Domain Operations**
* **Product management** — full CRUD, with optimistic locking (`@Version`) to prevent lost updates under
  concurrent stock changes, plus a dedicated low-stock query.
* **Customer management** — full CRUD for customers eligible for credit sales.
* **Sales** — cash (À Vista) and credit (A Prazo) sales, with stock validation/deduction, automatic
  subtotal/total calculation, credit-sale rules (a valid customer and due date are required), and queries by
  date range.

**Frontend**
* **Full CRUD UI** for Products and Customers (list, create, edit, delete), plus a low-stock view.
* **Sales workflow** — register cash or credit sales with a dynamic item list, list sales, filter by date
  range, and expand a sale to see its line items.
* **Dashboard** — a landing page summarizing totals (products, customers, sales, low-stock count, total sold).
* **Automated tests** — Vitest + React Testing Library, covering pure logic, UI interaction, and
  component behavior against a mocked API layer.

**Architecture & Quality**
* **Layered architecture** — controllers stay thin, business rules live in the service layer, and entities
  are never exposed directly: dedicated request/response DTOs carry Bean Validation everywhere.
* **Centralized error handling** — a single `@RestControllerAdvice` maps domain exceptions, validation
  failures, malformed requests, and concurrency conflicts to consistent, structured JSON error responses
  (`404`/`400`/`409`) instead of leaking stack traces.
* **Automated test suite** — 71 tests across four layers: Mockito unit tests for business rules, `@DataJpaTest`
  for custom queries, `@WebMvcTest` for HTTP-layer behavior, and a full-context integration test proving that
  Hibernate's dirty-checking actually persists stock changes, not just an in-memory mutation.
* **CI/CD** — GitHub Actions runs both the backend (Maven) and frontend (Vitest) test suites on every
  push/PR to `main`/`dev`; merging a `release/*` PR into `main` auto-creates the GitHub tag and Release,
  reading the version straight from `CHANGELOG.md`. Backend deployed as a Docker container on Render, built
  from a multi-stage `Dockerfile`; frontend deployed as a static build on Vercel.

## 🛠️ Tech Stack

**Backend**
* **Framework:** Spring Boot 4.1, Spring Data JPA, Spring Web MVC
* **Language:** Java 21 (Eclipse Temurin LTS)
* **Database:** PostgreSQL (hosted on Supabase) for the running app; H2 in-memory for the test suite
* **Testing:** JUnit 5, Mockito, AssertJ
* **API Docs:** springdoc-openapi (Swagger UI)
* **Deployment:** Render (Docker runtime)

**Frontend**
* **Framework:** React 19, TypeScript, Vite
* **Styling:** Tailwind CSS
* **Routing:** React Router
* **Testing:** Vitest, React Testing Library
* **Deployment:** Vercel

**Shared / DevOps**
* **Build/DevOps:** Maven, npm, Docker, GitHub Actions, GitFlow (Semantic Versioning)

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

### Frontend

Requires Node.js.

```bash
cd frontend
npm install
npm run dev
```

The app starts on `http://localhost:5173` and points at `http://localhost:8080` by default (see
`frontend/.env.development`). Run its test suite with:

```bash
npm test
```

## 🔮 Roadmap

* **Workflow Automation & AI Integration** — automated daily sales reports (email/WhatsApp) and predictive AI
  models over sales history for inventory alerts.

---
👨‍💻 **Author:** Daniel Farias Barreto de Moura
