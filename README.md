# SalesSystem

A robust, console-based application developed in Java SE for managing products, customers, and sales.

## 🧭 Project Evolution

This repository tracks the full evolution of this system in a single git history — from a pure Java SE
console app to a Spring Boot REST API:

* **[`v1.0.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.0.0)** — Initial Java SE console app: core OOP design, flat-file persistence.
* **[`v1.2.0`](https://github.com/danbarretom/SalesSystem/releases/tag/v1.2.0)** — Final Java SE version, 100% test-covered. **The rest of this README describes this version.**
* **`v2.0.0`** *(in progress, on [`feature/v2-spring-boot`](https://github.com/danbarretom/SalesSystem/tree/feature/v2-spring-boot))* — Spring Boot REST API rewrite: Spring Data JPA, PostgreSQL, Bean Validation, global exception handling, optimistic locking, full CRUD via `/api/{clientes,produtos,vendas}`.

## 🚀 Features

**Domain Operations:**
* **Product Management:** Full CRUD for inventory items.
* **Customer Management:** Full CRUD for registering and managing clients.
* **Sales Operations:** Support for Cash Sales (À Vista) and Credit Sales (A Prazo) with automatic inventory deduction and real-time customer validation.
* **Temporal Validation:** Uses `java.time.LocalDate` to prevent past due dates.

**🛡️ Architecture & Quality:**
* **Clean Code & Separation of Concerns:** Business logic (Managers) is strictly isolated from the presentation layer (View).
* **100% Test Coverage:** All business rules and managers are fully covered by unit tests.
* **Robust Error Handling:** Custom exceptions (`EntidadeNaoEncontradaException`, `RegraNegocioException`) and implementation of *Try-with-Resources* to prevent memory leaks and handle file manipulation safely.
* **CI/CD Pipeline:** Automated workflows using GitHub Actions for continuous integration (running tests automatically on push) and continuous delivery (automated release generation).

## 🛠️ Tech Stack

* **Language:** Java SE 21 (OpenJDK)
* **Paradigm:** Object-Oriented Programming (OOP)
* **Testing:** JUnit 5
* **DevOps:** GitHub Actions, GitFlow (Semantic Versioning)
* **Data Persistence:** Local flat files (`.txt`) separated by semicolons (`;`).

## 💻 Running the Application

1. Clone this repository: `git clone https://github.com/danbarretom/SalesSystem.git`
2. Navigate to the directory containing the compiled `.jar` file.
3. Run the executable using the terminal: `java -jar ControleVendas.jar`

*Note: The application will automatically generate the required `.txt` files in the root directory upon its first execution.*

## 🔮 Future Enhancements (Roadmap)

While the current CLI version successfully implements core OOP, automated testing, and persistence concepts, the architecture was designed with scalability in mind. The RESTful API / relational database evolution is already underway — see [🧭 Project Evolution](#-project-evolution) above. Remaining planned work:

* **Workflow Automation & AI Integration:** Integrating automation tools (like n8n or Zapier) to generate and send daily sales reports automatically via Email or WhatsApp, and applying predictive AI models to analyze sales history for inventory alerts.

---
👨‍💻 **Author:** Daniel Farias Barreto de Moura