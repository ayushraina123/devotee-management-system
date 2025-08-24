# Devotee Management System

[![Java](https://img.shields.io/badge/Java-23-blue)](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-green)](https://spring.io/projects/spring-boot)

A robust **Spring Boot 3.5.0** application utilizing **Java 23** to manage devotees, addresses, donations, and expenses efficiently. Supports REST APIs, batch jobs, Excel export, caching, and JWT-based role-based security.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [APIs](#apis)
- [Batch Jobs](#batch-jobs)
- [Caching](#caching)
- [Security](#security)
- [Setup & Installation](#setup--installation)
- [Future Scope](#future-scope)

---

## Project Overview

**Devotee Management System** simplifies managing devotees, their donations, addresses, and expenses.  
Supports **real-time CRUD operations** via REST APIs and **large-scale batch processing** for data cleanup and Excel export.

**Key Goals:**
- Maintain a clean database of devotees, donations, addresses, and expenses.
- Efficiently process large datasets using Spring Batch.
- Ensure fast API responses with caching mechanisms.
- Secure the system using JWT-based authentication and role-based authorization.

---

## Features

- **CRUD Operations:** Devotees, Addresses, Donations, Expenses.
- **Batch Jobs:**
    - Delete records from multiple entities.
    - Export data to Excel.
- **Caching:** Caffeine cache for frequently accessed queries.
- **Security:** JWT-based authentication and role-based authorization.
- **Validation & Exception Handling:** Global exception handling for APIs.
- **JPA/Hibernate:** Optimized database interactions with Spring Data JPA.

---

## Technology Stack

- **Backend:** Java 23, Spring Boot 3.5.0
- **Batch Processing:** Spring Batch
- **Database:** PostgreSQL
- **Caching:** Caffeine
- **Security:** JWT with role-based authorization
- **Build Tool:** Maven
- **Excel Handling:** Apache POI

---

## APIs

**Devotee API:**

GET /devotees
POST /devotees
PATCH /devotees/{id}


**Address API:**  

GET /addresses
POST /addresses


**Donation API:**


GET /donations
POST /donations


**Expense API:**


GET /expenses
POST /expenses


All APIs include validation and return standardized response structures.

---

## Batch Jobs

Spring Batch handles large-scale data operations efficiently:

1. **Delete Job**
    - Deletes all records from Address, Devotee, Donation, and Expense tables.
2. **Excel Export Job**
    - Exports data to Excel sheets.

Jobs can be triggered via API.

---

## Caching

- Frequently accessed queries are cached using **Caffeine Cache**.
- Automatic cache invalidation on data updates.

---

## Security

- JWT-based authentication.
- Role-based authorization restricts API access based on user roles.
- Only authorized users can perform CRUD or batch operations.

---

## Setup & Installation

### 1. Clone the repository
```bash
git clone https://github.com/ayushraina123/devotee-management-system.git
cd devotee-management-system
```

### Configure `application.properties`
- Update PostgreSQL database URL, username, and password.

### Build & Run
```bash
mvn clean install
mvn spring-boot:run
```
### 4. Access APIs
- Use Postman or any REST client: [http://localhost:8080](http://localhost:8080)

---

## Future Scope
- Add a **frontend interface** (React/Angular) for user interaction.
- Enhance batch jobs with advanced validation and reporting.
- Extend role-based authorization with more granular permissions.


