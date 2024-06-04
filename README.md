# Numarcis Backend Assignment

## Overview

This project showcases a backend system built using Spring Boot microservices architecture. The system consists of five main components:

1. **User Service**: Manages user registrations and login.
2. **Product Service**: Manages product handling.
3. **Order Service**: Manages orders.
4. **API Gateway**: Handles routing for the microservices.
5. **Eureka Discovery Service**: Manages service registration and discovery.

Each service is designed to be modular and communicate with each other securely using JWT-based authentication. 
The repository also includes integration tests.

## Table of Contents

- [Project Structure](#project-structure)
- [Endpoints and Functionality](#endpoints-and-functionality)
  - [User Service](#user-service)
  - [Product Service](#product-service)
  - [Order Service](#order-service)
  - [API Gateway](#api-gateway)
  - [Eureka Discovery Service](#eureka-discovery-service)
- [Security](#security)
- [Inter-Service Communication](#inter-service-communication)
- [Database](#database)

## Project Structure

The project is organized as a monorepo containing five Spring Boot microservices:

- `user-service`: Handles user-related operations.
- `product-service`: Handles product-related operations.
- `order-service`: Handles order-related operations.
- `api-gateway`: Handles routing.
- `eureka-server`: Handles service registration and discovery.

## Endpoints and Functionality

### User Service

- **Endpoints**:
  - `POST /register`: Register a new user with roles.
  - `POST /login`: Authenticate a user and issue a JWT.

- **Features**:
  - User registration with role assignment (ADMIN, USER).
  - Secure login with JWT authentication.
  - User information stored in a database.

### Product Service

- **Endpoints**:
  - `GET /products`: Retrieve a list of products.
  - `POST /products`: Create a new product (ADMIN only).
  - `PUT /products/{id}`: Update an existing product (ADMIN only).
  - `DELETE /products/{id}`: Delete a product (ADMIN only).
  - `GET /products/search`: Search products by name, description, or category.

- **Features**:
  - CRUD operations for products.
  - Search functionality for products based on name, description and/or category.

### Order Service

- **Endpoints**:
  - `POST /orders`: Create a new order.
  - `GET /orders`: Retrieve all orders for the authenticated user.
  - `GET /orders/{id}`: Retrieve a specific order (accessible by the order creator or ADMIN).
  - `PUT /orders/{id}`: Update an existing order (accessible by the order creator or ADMIN).
  - `DELETE /orders/{id}`: Delete an order (accessible by the order creator or ADMIN).

- **Features**:
  - CRUD operations for orders.
  - Orders reference users and products.
  - Endpoint to retrieve orders for a specific user.

### API Gateway

- **Features**:
  - Routes incoming requests to the appropriate microservice.
  - Propagates request for login and register without authorization, but checks if JWT token is present and valid for other paths.

### Eureka Discovery Service

- **Features**:
  - Manages service registration and discovery.
  - Keeps track of all running instances of microservices.

## Security

- **JWT Authentication**:
  - All services are secured with JWT-based authentication.
  - Endpoints are protected based on user roles.
  - Product Service: Only ADMINs can create, update, or delete products.
  - Order Service: Only the order creator or an ADMIN can view or modify an order.
 
- **Note**:
  - The UserService returns a valid JWT after successfull *authentication*
  - That JWT should be used for all further communication placed in the Header
  - The JWT is checked by other services therfore they have *authorization* implemented
      - Downside to this -> All services need to be configured with the same JWT secret
      - Positive side to this -> Other services don't need to communicate with UserService for each authorization check and the JWT hold all the necessary information like username and role

## Inter-Service Communication

- **Interaction Examples**:
  - Order Service verifies product availability with Product Service before placing an order.

## Database

- **H2 Database**:
  - An embedded H2 database is used for simplicity.
  - Can be replaced with another database if preferred.

## Executables

- Executable jar files can be found here -> [Numarcis Backend Assignment Executables](https://github.com/filiph-r/Numarcis-Backend-Assignment/tree/main/Executables)
- The **start_services.sh** bash script has been created to start all services gracefully
- make sure to run *chmod +x start_services.sh* before running the script


