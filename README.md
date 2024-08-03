# Estimator

Estimator is an application designed to estimate the cost of projects based on utilized technologies, man-hours, risk factors, and a variety of other parameters.

## Description

Estimator allows users to evaluate project costs based on multiple inputs such as:

- Utilized technologies
- Man-hours
- Risk factors
- Other variables impacting the final cost

This application assists project managers, developers, and clients in obtaining accurate project cost estimates during the early planning stages.

## Key Features

- Project cost estimation based on user-provided parameters
- Consideration of risks and their impact on costs
- Flexible settings for different project types
- Intuitive and user-friendly interface
- Ability to save and export estimates

## System Requirements

- Java 11+
- MySQL 8.0+
- Gradle 7.0+
- Payara Server 5.2020+
- Docker 20.10+

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/estimator.git
    cd estimator
    ```

2. Set up the MySQL database:

    ```sql
    CREATE DATABASE estimator;
    CREATE USER 'estimatoruser'@'localhost' IDENTIFIED BY 'password';
    GRANT ALL PRIVILEGES ON estimator.* TO 'estimatoruser'@'localhost';
    FLUSH PRIVILEGES;
    ```

3. Configure the `application.properties` file for database connection:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/estimator
    spring.datasource.username=estimatoruser
    spring.datasource.password=password
    ```

4. Build and run the application:

    ```sh
    ./gradlew build
    ./gradlew bootRun
    ```

## Usage

1. Open your browser and navigate to `http://localhost:8080`.
2. Create a new project, add parameters, and obtain a cost estimate.

