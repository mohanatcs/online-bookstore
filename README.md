# Online-Bookstore

## Overview

The Online Bookstore Backend is a Spring Boot-based REST API for managing
users, books, shopping carts, and orders. The application provides APIs for user registration and login, book
management, shopping cart operations, and checkout/order management.
The application uses H2 database for persistence and Swagger/OpenAPI
for interactive API documentation. The project follows a TDD approach with JUnit 5 and Mockito for unit testing.

## Project Scope

- Provide REST APIs for an online bookstore.
- Allow users to register and login.
- Allow users to browse and manage books.
- Allow users to add, update, and remove books from their cart.
- Allow users to checkout and create orders.
- Maintain order history after checkout.
- Provide API documentation using Swagger.
- Provide validation and centralized exception handling


----

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
---

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

---

## Getting Started

---


### Prerequisites

- **Java 17**
- **Maven**
- **Git**
- **IntelliJ IDEA**

### Clone the Repository
````
git clone https://github.com/mohanatcs/online-bookstore.git
````
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

You can access the Swagger UI documentation at:

```text
http://localhost:8081/swagger-ui.html
```
## API Reference

---
### Authentication

#### Register User

- **URL:** `/api/auth/register`
- **Method:** `POST`
- **Request Body:**

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

- **Response:** Returns a success message when the user is registered.

#### Login User

- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Request Body:**

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

- **Response:** Returns login confirmation and username.

### Books

#### Get All Books

- **URL:** `/api/books`
- **Method:** `GET`
- **Response:** Returns the list of available books.

#### Create Book

- **URL:** `/api/books/create`
- **Method:** `POST`
- **Request Body:**

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "price": 499.99
}
```

- **Response:** Returns the created book.

### Shopping Cart

#### Add Book to Cart

- **URL:** `/api/cart/create/items`
- **Method:** `POST`
- **Request Body:**

```json
{
  "userId": 1,
  "bookId": 1,
  "quantity": 2
}
```

- **Response:** Returns the updated cart.

#### View Cart

- **URL:** `/api/cart/{userId}`
- **Method:** `GET`
- **Response:** Returns the user's cart with items and total price.

### Orders

#### Create Order

- **URL:** `/api/orders/create`
- **Method:** `POST`
- **Request Body:**

```json
{
  "userId": 1
}
```

- **Response:** Returns the created order with order items and total price.

#### Get Order

- **URL:** `/api/orders/{orderId}`
- **Method:** `GET`
- **Response:** Returns the order details.

#### Get User Orders

- **URL:** `/api/orders/user/{userId}`
- **Method:** `GET`
- **Response:** Returns all orders for the specified user.

### Error Responses

In case of errors, the API returns a standardized JSON error response.

Example:

```json
{
  "status": 404,
  "message": "Book not found"
}
```

Common HTTP status codes:

- **200** – Successful request
- **201** – Resource created
- **400** – Bad request / validation error
- **401** – Invalid credentials
- **404** – Resource not found