# SalesSystem

A robust, console-based application developed in Java SE for managing products, customers, and sales.

## 🚀 Features

- **Product Management:** Full CRUD for inventory items.
- **Customer Management:** Full CRUD for registering and managing clients.
- **Sales Operations:**
    - Support for Cash Sales (À Vista) and Credit Sales (A Prazo).
    - Automatic inventory deduction upon successful transactions.
    - Real-time customer validation for credit sales.
    - Temporal validation using `java.time.LocalDate` to prevent past due dates.
- **Robust Input Handling:** Custom exception handling to prevent buffer overflow and crashes caused by invalid user inputs (e.g., typing letters instead of numbers).

## 🛠️ Tech Stack

- **Language:** Java SE (OpenJDK 25)
- **Paradigm:** Object-Oriented Programming (OOP)
- **Data Persistence:** Local flat files (`.txt`) separated by semicolons (`;`).

### Running the Application
1. Clone this repository:
   ```bash
   git clone https://github.com/danbarretom/SalesSystem.git
Navigate to the directory containing the compiled .jar file

2. Run the executable using the terminal:

```bash
java -jar ControleVendas.jar
```

Note: The application will automatically generate the required .txt files (produtos_prova.txt, etc.) in the root directory upon its first execution.

## 🔮 Future Enhancements (Roadmap)

While the current CLI version successfully implements core OOP and persistence concepts, the architecture was designed with scalability in mind. The planned evolutions for this system include:

- **Relational Database Migration:** Replacing the `.txt` file persistence with a robust SQL database (e.g., MySQL or PostgreSQL) using JDBC to support concurrent transactions and improve data integrity.
- **RESTful API Conversion:** Evolving the monolithic console application into a backend microservice using the **Spring Boot** framework. This will allow the system to communicate via JSON with modern Web or Mobile frontends.
- **Workflow Automation & AI Integration:** - Integrating automation tools (like **n8n** or Zapier) to generate and send daily sales reports automatically via Email or WhatsApp.
    - Applying predictive AI models to analyze sales history and automatically trigger alerts when inventory is projected to drop below minimum thresholds.

👨‍💻 Author
Daniel Farias Barreto de Moura