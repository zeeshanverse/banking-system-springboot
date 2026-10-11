# Banking System — Spring Boot REST API

> **A production-oriented Java/Spring Boot banking backend focused on secure REST APIs, transactional money movement, concurrency correctness, Redis caching, rate limiting, and asynchronous reporting.**

A secure, backend-focused banking application built with **Java, Spring Boot, PostgreSQL, Redis, Spring Security, JWT, JPA/Hibernate, JdbcTemplate, Flyway, Bucket4j, and Docker**.

The project demonstrates how to build a banking API where **money movement is a transactional and concurrency-sensitive problem**, not simply CRUD. Deposits, withdrawals, and transfers use database transactions and pessimistic row locking, while transfers acquire account locks in a deterministic order to prevent opposite-transfer deadlocks.

---

## ⚡ Recruiter Quick View — 30–60 Seconds

### What is this?

A **secure RESTful banking backend** that supports:

- JWT authentication and BCrypt password hashing
- SAVINGS/CURRENT account creation
- Deposits, withdrawals, and account-to-account transfers
- Transaction history and asynchronous transaction reports
- PostgreSQL persistence with Flyway migrations
- Redis-backed caching
- Bucket4j rate limiting
- Spring Security authorization
- Swagger/OpenAPI documentation
- Dockerized PostgreSQL + Redis development environment

### What makes it technically interesting?

The core engineering challenge is **concurrent money movement**.

For transfers, the application:

1. Starts a database transaction.
2. Locks both account rows using `PESSIMISTIC_WRITE` / `SELECT ... FOR UPDATE`.
3. Acquires locks in **ascending account-ID order**.
4. Validates ownership and available balance.
5. Updates both balances and records the transaction.
6. Commits atomically and releases the locks.

This prevents race conditions around balance updates and avoids the classic A→B / B→A deadlock pattern.

### Engineering highlights

**Security →** JWT + Spring Security + BCrypt  
**Data →** PostgreSQL + JPA/Hibernate + JdbcTemplate + Flyway  
**Concurrency →** pessimistic locking + deterministic lock ordering  
**Performance →** Redis caching + dedicated async report executor  
**Resilience →** Bucket4j per-user/IP rate limiting  
**API →** REST + Swagger/OpenAPI  
**Operations →** Docker Compose + Actuator  
**Testing →** 60+ JUnit/Mockito/Spring Security tests

> **Portfolio note:** The repository is intentionally backend-focused. Swagger/OpenAPI acts as the interactive API client, so the system can be demonstrated without a separate frontend.

---

## ✨ Features

- **Authentication:** registration and login with JWT; stateless Spring Security; BCrypt password hashing.
- **Authorization:** authenticated users can only access resources they own.
- **Accounts:** create SAVINGS or CURRENT accounts.
- **Money movement:** deposit, withdraw, and transfer operations execute inside database transactions.
- **Concurrency control:** JPA `PESSIMISTIC_WRITE` / PostgreSQL `SELECT ... FOR UPDATE` with a 5-second lock timeout.
- **Deadlock prevention:** transfers acquire both account locks in ascending account-ID order.
- **Transaction ledger:** successful money movements are recorded with `SUCCESS` status.
- **Reporting:** transaction reports use `JdbcTemplate` and a multi-table SQL join.
- **Async reports:** reports execute on a dedicated executor with `core 3`, `max 6`, and `queue 50`.
- **Caching:** Redis-backed Spring Cache for selected account, user, and transaction reads.
- **Rate limiting:** Bucket4j token buckets keyed by authenticated user or IP.
- **Validation:** Bean Validation for request payloads.
- **Global errors:** centralized exception handling with typed API exceptions.
- **Database migrations:** Flyway migrations for users, accounts, and transactions.
- **API documentation:** Swagger UI + OpenAPI 3.1.
- **Operations:** Spring Boot Actuator health/info endpoints.
- **Containerized dependencies:** Docker Compose for PostgreSQL and Redis.
- **Testing:** 10 test classes with 60+ JUnit/Mockito/Spring Security tests.

---

## 🧰 Tech Stack

| Area | Technologies |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.5 |
| Security | Spring Security, JWT (`jjwt 0.12.6`), BCrypt |
| Persistence | Spring Data JPA, Hibernate, JdbcTemplate |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Cache | Spring Cache, Redis 7 |
| Rate limiting | Bucket4j + Caffeine-backed token buckets |
| Async processing | `ThreadPoolTaskExecutor` |
| API documentation | SpringDoc OpenAPI / Swagger UI |
| Monitoring | Spring Boot Actuator |
| Testing | JUnit 5, Mockito, Spring Security Test |
| Infrastructure | Docker Compose |
| Build | Maven Wrapper |

> Java 17 is the project baseline. Virtual threads are enabled through Spring Boot and are available when running the application on JDK 21+.

---

## 🏗️ Architecture

```mermaid
flowchart LR
    C[Client / Swagger UI] --> F[JWT Filter]
    F --> RL[Rate Limiter]
    RL --> CT[REST Controllers]
    CT --> S[Service Layer]

    S --> JPA[JPA Repositories]
    S --> JDBC[JdbcTemplate Reporting]
    S --> CACHE[Redis Cache]
    S --> ASYNC[Async Report Executor]

    JPA --> DB[(PostgreSQL)]
    JDBC --> DB
    ASYNC --> JDBC
    DB --> MIG[Flyway Migrations]