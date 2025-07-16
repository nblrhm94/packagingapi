# Package Shipping API

This is the backend service for the Package Shipping application. It is a Spring Boot application that provides a REST API to manage product information and process packaging orders based on a set of business rules.

The core responsibility of this API is to take a list of selected products and intelligently group them into one or more packages, optimizing for price and weight constraints.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [1. Database Setup](#1-database-setup)
    - [2. Application Configuration](#2-application-configuration)
    - [3. Running the Application](#3-running-the-application)
- [API Documentation (Swagger UI)](#api-documentation-swagger-ui)
- [API Endpoints](#api-endpoints)

## Features

- RESTful API for fetching products and placing orders.
- Automatic database table creation and data seeding on first run.
- Business logic to split orders into packages based on the following rules:
    1. Orders with a total cost over $250 are split into multiple packages.
    2. No single package's contents can have a total cost of $250 or more.
- Integrated Swagger UI for interactive API documentation and testing.

## Technology Stack

- **Framework**: [Spring Boot 3](https://spring.io/projects/spring-boot)
- **Language**: [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Build Tool**: [Maven](https://maven.apache.org/)
- **Database**: [MySQL](https://www.mysql.com/)
- **Data Access**: [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- **API Documentation**: [SpringDoc OpenAPI (Swagger UI)](https://springdoc.org/)

## Prerequisites

Before you begin, ensure you have the following installed on your system (instructions are tailored for macOS with [Homebrew](https://brew.sh/)):

- **Java 17**: `brew install openjdk@17`
- **Maven**: `brew install maven`
- **MySQL**: `brew install mysql`
- **Git**: `brew install git`
- **An IDE**: [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended.

## Getting Started

Follow these steps to get the application up and running on your local machine.

### 1. Database Setup

The application requires a MySQL database and a dedicated user.

1.  **Start the MySQL service:**
    ```bash
    brew services start mysql
    ```

2.  **Log in to MySQL as the root user:**
    ```bash
    mysql -u root -p
    ```
    *(You will be prompted for the root password you set during installation.)*

3.  **Execute the following SQL commands** to create the database and user. Replace `'your_secure_password'` with a password of your choice.
    ```sql
    CREATE DATABASE packaging_db;
    CREATE USER 'pkg_user'@'localhost' IDENTIFIED BY 'your_secure_password';
    GRANT ALL PRIVILEGES ON packaging_db.* TO 'pkg_user'@'localhost';
    FLUSH PRIVILEGES;
    EXIT;
    ```

### 2. Application Configuration

1.  Navigate to the configuration file at `src/main/resources/application.properties`.

2.  Update the database credentials to match what you created in the previous step.
    ```properties
    # ... other properties ...
    spring.datasource.username=pkg_user
    spring.datasource.password=your_secure_password
    ```

### 3. Running the Application

There are two primary ways to run the application:

#### From your IDE (Recommended)

1.  Open the project in IntelliJ IDEA.
2.  Wait for Maven to download all dependencies.
3.  Locate the `PackagingApiApplication.java` file in `src/main/java/com/shipping/packagingapi/`.
4.  Right-click the file and select **`Run 'PackagingApiApplication.main()'`**.

#### From the Command Line

1.  Open a terminal and navigate to the root directory of the `packaging-api` project.
2.  Run the following Maven command:
    ```bash
    ./mvnw spring-boot:run
    ```

The application will start and be available at **`http://localhost:8080`**.

## API Documentation (Swagger UI)

This project includes integrated Swagger UI for live, interactive API documentation. Once the application is running, you can access it in your browser.

- **Swagger UI URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

From this interface, you can see all available endpoints, their required parameters, and example responses. You can also execute API calls directly from the browser, which is extremely useful for testing and debugging.

## API Endpoints

Here is a summary of the available API endpoints:

#### Fetch All Products

- **URL**: `/api/products`
- **Method**: `GET`
- **Description**: Retrieves a list of all available products from the database, sorted alphabetically by name.
- **Success Response**: `200 OK`
  ```json
  [
    {
      "id": 1,
      "name": "Item 1",
      "price": 10.0,
      "weight": 200
    },
    ...
  ]
  
### Place an Order

- **URL**: `/api/orders/package`
- **Method**: `POST`
- **Description**: Takes a list of product IDs and returns a list of packages created according to the business rules.

  ```json
  "productIds": [1, 14, 27]

- **Success Response**: `200 OK`
  
  ```json
    [
      {
        "items": ["Item 14", "Item 1"],
        "totalWeight": 220,
        "totalPrice": 250.0,
        "courierPrice": 10.0
      },
      {
        "items": ["Item 27"],
        "totalWeight": 790,
        "totalPrice": 99.0,
        "courierPrice": 15.0
      }
    ]