# Banking System – Spring Boot REST API

A backend banking system built with **Java, Spring Boot, and PostgreSQL**. The project provides REST APIs for user authentication, account management, deposits, withdrawals, money transfers, and transaction history.

## Key Features

* User registration and JWT-based authentication
* Bank account creation and management
* Deposit, withdrawal, and money transfer operations
* PostgreSQL database with Spring Data JPA
* JDBC/JdbcTemplate-based transaction reporting
* Centralized exception handling and validation
* Concurrent transaction processing and database locking
* Asynchronous report generation using a thread pool
* Unit and integration testing
* Swagger/OpenAPI documentation

## Tech Stack

* **Language:** Java
* **Framework:** Spring Boot
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Database Access:** JDBC / JdbcTemplate
* **Security:** Spring Security, JWT
* **Testing:** JUnit, Mockito
* **Build Tool:** Maven
* **API Documentation:** Swagger/OpenAPI
* **Version Control:** Git/GitHub

## Project Structure

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

For reporting operations, JDBC is used directly through `JdbcTemplate`.

## Main REST APIs

* Authentication
* User management
* Account management
* Deposits
* Withdrawals
* Transfers
* Transaction history
* Asynchronous transaction reports

## Running the Project

1. Configure PostgreSQL credentials in the application configuration.
2. Start the PostgreSQL database.
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

The API can then be accessed locally through the configured Spring Boot port.

## Purpose

This project was developed as a practical backend project to strengthen my understanding of **Java, OOP, Spring Boot, REST APIs, PostgreSQL, JDBC, exception handling, database transactions, and concurrent processing**.
