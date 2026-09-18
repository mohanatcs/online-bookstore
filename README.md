# Online-Bookstore

## Overview

The Online Bookstore Backend is a Spring Boot-based REST API for managing
users, books, shopping carts, and orders.

The application provides APIs for user registration and login, book
management, shopping cart operations, and checkout/order management.

The application uses H2 database for persistence and Swagger/OpenAPI
for interactive API documentation.

The project follows a TDD approach with JUnit 5 and Mockito for unit testing.

## Project Scope

- Provide REST APIs for an online bookstore.
- Allow users to register and login.
- Allow users to browse and manage books.
- Allow users to add, update, and remove books from their cart.
- Allow users to checkout and create orders.
- Maintain order history after checkout.
- Provide API documentation using Swagger.
- Provide validation and centralized exception handling.

## Features

- **User Authentication:**
  Provides user registration and login with BCrypt password encryption and request validation.

- **Book Management:**
  Provides REST APIs to create, retrieve, update, and delete books.

- **Shopping Cart:**
  Allows users to add books, view the cart, update quantities, and remove items.

- **Order Management:**
  Creates orders from the shopping cart, calculates the order total, and maintains order history.

- **Checkout:**
  Converts cart items into order items and clears the cart after successful checkout.

- **OpenAPI Documentation:**
  Provides interactive API documentation using Swagger UI.

- **Global Error Handling:**
  Provides centralized exception handling with standardized HTTP error responses.

- **Unit Testing:**
  Uses JUnit 5 and Mockito with a Test Driven Development (TDD) approach.

## High-Level Architecture

```text
Client
   |
   v
Controller Layer
   |
   v
DTO Layer
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
H2 Database
```

### Layers

- **Controller:** Handles HTTP requests and responses.
- **DTO:** Defines API request and response models.
- **Service:** Contains business logic.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents database tables.
- **Exception:** Provides centralized error handling.
- **Config:** Contains security and OpenAPI configuration.

## Technology Stack

- Java 17
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database
- Maven
- Lombok
- Swagger / OpenAPI
- JUnit 5 & Mockito

## API Documentation

Swagger / OpenAPI is used for interactive API documentation.

API documentation is maintained directly at the controller level using
OpenAPI annotations. This keeps the documentation close to the API
implementation and makes it easier to maintain and understand.

Each API includes relevant information such as:

- API description
- Request and response details
- HTTP status codes
- Validation and error responses

Swagger UI is available at:

```text
http://localhost:8081/swagger-ui.html
```

The OpenAPI specification is maintained in:

```text
src/main/resources/openapi.yml
```

## Error Handling

The application uses centralized exception handling through
`GlobalExceptionHandler`.

Handled scenarios include:

- Validation errors
- User not found
- Book not found
- Cart not found
- Cart item not found
- Order not found
- Invalid login credentials

Common HTTP status codes:

- **200** – Successful request
- **201** – Resource created
- **400** – Bad request / validation error
- **401** – Invalid credentials
- **404** – Resource not found

## Testing & TDD

The project follows a Test Driven Development approach:

```text
Write Test
    ↓
Run Test
    ↓
Implement Code
    ↓
Run Test
    ↓
Refactor
```

Unit tests are implemented using JUnit 5 and Mockito.

The service layer is tested for successful operations, business logic,
validation scenarios, and exception scenarios.

Run the tests using:

```bash
mvn test
```

## Code Quality

- Uses Lombok to reduce boilerplate code.
- Uses DTOs to separate API models from persistence entities.
- Uses constructor-based dependency injection.
- Uses centralized exception handling.
- Follows layered architecture for separation of responsibilities.

## Getting Started

### Prerequisites

- **Java 17**
- **Maven**
- **Git**
- **IntelliJ IDEA**

### Clone the Repository

```bash
git clone https://github.com/mohanatcs/online-bookstore.git
cd online-bookstore
```

### Build the Project

Use Maven to build the project:

```bash
mvn clean install
```

## Launch the Application

Start the application with:

```bash
mvn spring-boot:run
```

The application will start on port **8081**.

### Swagger UI

```text
http://localhost:8081/swagger-ui.html
```

### H2 Console

```text
http://localhost:8081/h2-console
```
