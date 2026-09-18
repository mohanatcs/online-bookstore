# Online-Bookstore

## Overview

This project is the backend implementation of an Online Bookstore using Spring Boot and Java.

The backend provides REST APIs for:

- User registration and login
- Book management
- Shopping cart management
- Order and checkout processing
- Request validation
- Exception handling
- Swagger API documentation
- H2 database persistence
- Unit testing

The project is being developed using a TDD-oriented approach.

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

## Features

### Authentication
- User Registration
- User Login
- BCrypt password encryption
- Request validation
- Exception handling

### Book Management
- Create Book
- Get All Books
- Get Book by ID
- Update Book
- Delete Book

### Shopping Cart
- Add books to cart
- View cart
- Update book quantity
- Remove books from cart

### Order Management
- Create order / checkout
- Generate order summary
- View order details
- Calculate order total

## API Documentation

Swagger UI:
http://localhost:8081/swagger-ui.html

H2 Console:
http://localhost:8081/h2-console

## Testing

Unit tests implemented using JUnit 5 and Mockito for Authentication and Book Management.

## Run
mvn spring-boot:run

Application runs on:

http://localhost:8081