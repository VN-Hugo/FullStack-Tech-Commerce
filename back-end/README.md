# Tech Shop Backend

## Introduction

Tech Shop Backend is the server-side application for an e-commerce platform, built with Spring Boot and supporting product management, user authentication, shopping cart, order processing, and payment flow.

## Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Gradle
- Swagger / OpenAPI
- Docker

## Architecture

- Layered architecture with controller, service, repository, model, dto, and exception layers.
- Uses Spring Boot for dependency injection, transaction management, and REST API support.
- Relational database persistence with JPA/Hibernate.

## Main Features

- User registration and authentication (email/password and token-based refresh)
- Product CRUD and search
- Shopping cart management
- Order creation and order history
- Payment processing flow with payment records and order status updates
- Global exception handling

## API Documentation

API documentation is available via Swagger/OpenAPI configuration when the application is running.

## Project Structure

- `src/main/java/com/webapp/tech_shop`
  - `auth` - authentication and authorization endpoints and services
  - `product` - product catalog, search, and management
  - `cart` - shopping cart and cart item management
  - `order` - order creation, retrieval, and confirmation
  - `payment` - payment processing and payment history
  - `security` - JWT, OAuth2, and Spring Security configuration
  - `exception` - centralized error handling and custom exception types
  - `shared` - common base entities, DTOs, and utilities
- `src/main/resources`
  - `application.properties` - application configuration
  - `db/migration` - database schema and seed SQL

## Database Design

The database is PostgreSQL-based and currently includes tables for:

- `users`
- `tokens`
- `products`
- `brands`
- `categories`
- `carts`
- `cart_product`
- `orders`
- `order_detail`
- `payment_method`
- `payments`

## Security

- JWT-based authentication and refresh token support
- Spring Security configuration for protected APIs
- Password encoding with BCrypt

## Performance & Query Optimization

- Uses repository queries and fetch joins where needed
- Pagination support for product listing and search
- Further query optimization details: 

## Testing

- Currently includes starter test scaffolding in `src/test/java`
- Additional unit and integration tests should be added for services, repositories, and controllers

## Docker

Docker support is available via `DockerFile` and `docker-compose.yml` for containerized deployment.

## Environment Variables

The application reads configuration from environment variables, including:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET_KEY`
- Google OAuth2 client settings if enabled

## Getting Started

1. Set environment variables or create a `.env` file with database and JWT settings.
2. Run the application with Gradle:

```bash
./gradlew bootRun
```

3. Access APIs on `http://localhost:8081`

## Future Improvements

- Add full integration tests for the payment flow and order lifecycle
- Add more advanced product filtering and sorting
- Add support for multiple payment gateways
- Add admin dashboards and role-based authorization
