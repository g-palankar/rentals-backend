# Rentals Backend

A Spring Boot backend application for tenant management, enabling landlords to efficiently manage their properties, tenants, rent payments, and maintenance requests.

## Features

- **Tenant Management**: Add, update, and track tenant information
- **Property Management**: Manage rental properties and units  
- **Rent Payment Tracking**: Monitor rent payments and payment history
- **Maintenance Requests**: Handle maintenance requests and work orders
- **Secure Authentication**: OAuth2 integration with Auth0 for secure access

## Technology Stack

- **Framework**: Spring Boot 3.5.0
- **Security**: Spring Security with OAuth2 Resource Server
- **Database**: H2 (development), JPA/Hibernate for data persistence
- **Build Tool**: Maven
- **Java Version**: 24
- **Authentication**: Auth0 JWT tokens

## Getting Started

### Prerequisites

- Java 24 or higher
- Maven 3.6+
- Auth0 account for authentication setup

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd rentals-backend
```

2. Set up configuration:
```bash
cp src/main/resources/application-dev.yaml.example src/main/resources/application-dev.yaml
```

3. Update the configuration file with your Auth0 settings:
   - Edit `src/main/resources/application-dev.yaml`
   - Replace `YOUR_AUTH0_DOMAIN` with your actual Auth0 domain

4. Build and run the application:
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Development Database

- **H2 Console**: Available at `http://localhost:8080/public/h2-console`
- **Connection URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## Configuration

The application uses Spring Boot profiles:

- **Development**: `dev` profile (default)
- **Testing**: `test` profile  
- **Production**: `prod` profile

Configuration files are environment-specific and gitignored for security.

## Testing

Run the test suite:
```bash
./mvnw test
```

Run specific tests:
```bash
./mvnw test -Dtest=ClassName
./mvnw test -Dtest=ClassName#methodName
```

## Building for Production

Create a production JAR:
```bash
./mvnw clean package
java -jar target/rentals-backend-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests to ensure everything works
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.