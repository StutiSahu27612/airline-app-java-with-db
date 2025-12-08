# Airline Flight Management System

A RESTful API application built with Spring Boot for managing airline flight information. This application provides CRUD operations for flight management with MySQL database integration.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [CI/CD Pipeline](#cicd-pipeline)
- [Database Schema](#database-schema)
- [Docker Support](#docker-support)
- [Contributing](#contributing)

## ✨ Features

- Create, Read, Update, and Delete (CRUD) operations for flights
- RESTful API architecture
- MySQL database integration with JPA/Hibernate
- Docker containerization support
- Automated testing with JUnit
- Code coverage reporting with JaCoCo
- CI/CD pipeline with GitLab CI
- Cross-Origin Resource Sharing (CORS) enabled

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.0.11
- **Java Version**: 17
- **Build Tool**: Maven 3.8.6+
- **Database**: MySQL 8.x
- **ORM**: Spring Data JPA with Hibernate
- **Testing**: JUnit, Spring Boot Test
- **Code Coverage**: JaCoCo
- **Containerization**: Docker
- **CI/CD**: GitLab CI

## 📦 Dependencies

- `spring-boot-starter-web` - Web application support with embedded Tomcat
- `spring-boot-starter-data-jpa` - Database access using Spring Data JPA
- `mysql-connector-j` - MySQL database driver
- `lombok` - Reduces boilerplate code
- `spring-boot-starter-test` - Testing support

## 🔧 Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Maven 3.8.6 or higher
- MySQL 8.x
- Docker (optional, for containerized deployment)
- Git

## 📥 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/StutiSahu27612/airline-app-java-with-db.git
   cd airline-app-java-with-db
   ```

2. **Set up MySQL Database**
   ```sql
   CREATE DATABASE airline;
   ```

3. **Configure Environment Variables**
   ```bash
   export DB_USERNAME=your_mysql_username
   export DB_PASSWORD=your_mysql_password
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

## ⚙️ Configuration

### Application Properties

The application can be configured through `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=9000

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/airline?serverTimezone=UTC
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Docker Profile

For Docker deployment, use `application-docker.properties` configuration.

## 🚀 Running the Application

### Local Development

1. **Using Maven**
   ```bash
   mvn spring-boot:run
   ```

2. **Using Java**
   ```bash
   mvn clean package
   java -jar target/airline-app.jar
   ```

The application will start on `http://localhost:9000`

### Using Docker

1. **Build the Docker image**
   ```bash
   mvn clean package
   docker build -t airline-app .
   ```

2. **Run the container**
   ```bash
   docker run -p 9000:9000 \
     -e DB_USERNAME=your_username \
     -e DB_PASSWORD=your_password \
     airline-app
   ```

## 📡 API Endpoints

Base URL: `http://localhost:9000`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/flight/` | Create a new flight | Flight JSON |
| GET | `/flight/` | Get all flights | - |
| GET | `/flight/{flightId}` | Get flight by ID | - |
| PUT | `/flight/{flightId}` | Update flight by ID | Flight JSON |
| DELETE | `/flight/{flightId}` | Delete flight by ID | - |

### Request/Response Examples

**Create Flight (POST /flight/)**
```json
{
  "flightName": "Air India 101",
  "source": "Mumbai",
  "destination": "Delhi",
  "ticketPrice": 5500.00
}
```

**Response**
```json
{
  "flightId": 1,
  "flightName": "Air India 101",
  "source": "Mumbai",
  "destination": "Delhi",
  "ticketPrice": 5500.00
}
```

**Get All Flights (GET /flight/)**
```json
[
  {
    "flightId": 1,
    "flightName": "Air India 101",
    "source": "Mumbai",
    "destination": "Delhi",
    "ticketPrice": 5500.00
  }
]
```

## 🧪 Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report
```

### Code Coverage Report

After running tests with JaCoCo, view the coverage report at:
```
target/site/jacoco/index.html
```

### Available Tests

- **Unit Tests**: Controller and service layer tests
- **Integration Tests**: End-to-end API testing

## 🔄 CI/CD Pipeline

The project uses GitLab CI for continuous integration and deployment with the following stages:

1. **Test**: Runs unit and integration tests
2. **Code Coverage**: Generates JaCoCo coverage reports
3. **Build**: Packages the application as a JAR file
4. **Push**: Builds and pushes Docker image to GitLab Container Registry
5. **Deploy**: Deploys to Google Cloud Platform Artifact Registry

### Pipeline Configuration

The pipeline is configured in `.gitlab-ci.yml` with the following features:
- Automated testing on every commit
- Code coverage reporting
- Docker image creation and registry push
- Integration with Google Cloud Platform

## 📊 Database Schema

### Flight Table

| Column | Type | Description |
|--------|------|-------------|
| flight_id | INT (PK, Auto-increment) | Unique flight identifier |
| flight_name | VARCHAR | Name of the flight |
| source | VARCHAR | Departure city |
| destination | VARCHAR | Arrival city |
| ticket_price | DOUBLE | Price of the ticket |

## 🐳 Docker Support

### Dockerfile

The application includes a Dockerfile for containerization:

```dockerfile
FROM openjdk:17
WORKDIR /app
COPY target/airline-app.jar /app/airline-app.jar
EXPOSE 9000
CMD ["java", "-jar", "/app/airline-app.jar"]
```

### Build Commands

```bash
# Build JAR
mvn clean package

# Build Docker image
docker build -t airline-app:latest .

# Run container
docker run -p 9000:9000 airline-app:latest
```

## 📝 Project Structure

```
airline-app-java-with-db/
├── src/
│   ├── main/
│   │   ├── java/com/airline/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── AirlineApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-docker.properties
│   └── test/
│       └── java/com/airline/    # Test classes
├── pom.xml                      # Maven configuration
├── Dockerfile                   # Docker configuration
├── .gitlab-ci.yml              # CI/CD pipeline
└── README.md                    # Project documentation
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Coding Standards

- Follow Java coding conventions
- Write unit tests for new features
- Ensure all tests pass before submitting PR
- Update documentation as needed

## 📄 License

This project is available for educational and demonstration purposes.

## 👥 Authors

- Repository: [StutiSahu27612/airline-app-java-with-db](https://github.com/StutiSahu27612/airline-app-java-with-db)

## 🐛 Troubleshooting

### Common Issues

**Database Connection Error**
- Verify MySQL is running
- Check database credentials in environment variables
- Ensure database `airline` exists

**Port Already in Use**
- Change the port in `application.properties`
- Or stop the process using port 9000

**Build Failures**
- Ensure Java 17 is installed
- Clear Maven cache: `mvn clean`
- Verify Maven version: `mvn --version`

## 📞 Support

For issues and questions, please open an issue in the GitHub repository.

---

**Happy Coding! ✈️**
