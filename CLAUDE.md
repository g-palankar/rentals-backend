# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.0 rental management backend application for property owners to manage tenants. The application uses Java 24, Maven for build management, and Spring Boot with Spring Security OAuth2 resource server authentication via Auth0.

## Development Commands

### Build and Run
- `./mvnw clean compile` - Compile the application
- `./mvnw spring-boot:run` - Run the application locally (starts on port 8080 by default)
- `./mvnw clean package` - Build JAR artifact
- `java -jar target/rentals-backend-0.0.1-SNAPSHOT.jar` - Run the built JAR

### Testing
- `./mvnw test` - Run all tests
- `./mvnw test -Dtest=ClassName` - Run specific test class
- `./mvnw test -Dtest=ClassName#methodName` - Run specific test method

### Database
- H2 in-memory database is used for development
- H2 Console available at: `http://localhost:8080/public/h2-console`
- Connection details: `jdbc:h2:mem:testdb`, username: `sa`, password: `password`

## Architecture

### Package Structure
```
dev.ganeshpalankar.rentals_backend/
├── config/           # Configuration classes (SecurityConfig)
├── users/
│   ├── model/        # JPA entities (User)
│   ├── repository/   # Spring Data repositories (UserRepository)  
│   └── controller/   # REST controllers (UserController)
└── RentalsBackendApplication.java  # Main application class
```

### Security Configuration
- OAuth2 Resource Server with JWT tokens from Auth0
- Public endpoints: `/public/**`, `/v3/api-docs/**`, `/swagger-ui/**`
- All other endpoints require authentication
- CSRF disabled for API usage
- H2 console accessible at `/public/h2-console`

### Key Technologies
- **Framework**: Spring Boot 3.5.0 with Spring Security, Spring Data JPA, Spring Web
- **Database**: H2 (in-memory for development)
- **Authentication**: OAuth2 Resource Server with Auth0 JWT
- **Build Tool**: Maven
- **Java Version**: 24
- **Additional Libraries**: Lombok for boilerplate reduction, Spring Boot Actuator

### Configuration Files
The application uses Spring Boot profiles for environment-specific configuration:
- `application.yaml` - Base configuration with common properties (application name, default profile set to 'dev')
- `application-dev.yaml` - Development environment configuration (H2 database, Auth0 settings, debug logging)
- Profile can be changed using `spring.profiles.active` property or `-Dspring.profiles.active=profileName`

#### Setup Instructions
1. Copy `src/main/resources/application-dev.yaml.example` to `application-dev.yaml`
2. Update the Auth0 domain in the `jwk-set-uri` field with your actual Auth0 domain
3. Environment-specific config files are gitignored to keep sensitive data secure

### Current Implementation Status
- User entity with Spring Data JPA repository
- UserController with REST endpoints
- Security configuration for OAuth2 with Auth0 integration