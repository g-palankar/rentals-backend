# Common Package - Response and Exception Handling

This package contains the standardized response and exception handling infrastructure for the rentals backend application.

## Package Structure

```
common/
├── response/
│   ├── ApiResponse.java           # Success response wrapper
│   ├── ResponseBuilder.java       # Builder for success responses
│   ├── ErrorResponse.java         # Error response wrapper
│   ├── ErrorResponseBuilder.java  # Builder for error responses
│   ├── ErrorDetail.java           # Nested error information
│   └── FieldError.java            # Field-level validation errors
└── exception/
    ├── ErrorType.java             # Error type enumeration
    ├── ExceptionResponseHandler.java  # Handler interface
    └── GlobalExceptionHandler.java    # Central exception handling
```

## Response System

### Success Response Structure

All successful API responses follow this standardized format:

```json
{
  "status": 201,
  "message": "User created successfully",
  "data": {
    "id": 123,
    "externalId": "auth0|1234567890",
    "createdAt": "2023-12-25T10:15:30.123Z",
    "updatedAt": "2023-12-25T10:15:30.123Z"
  },
  "timestamp": "2023-12-25T10:15:30.456Z"
}
```

### Success Response Usage

```java
// In controllers
return ResponseBuilder.<User>create()
    .status(HttpStatus.CREATED)
    .message("User created successfully")
    .data(user)
    .build();
```

**Properties:**
- `status` - HTTP status code (200, 201, etc.)
- `message` - Human-readable success message
- `data` - The actual response payload (generic type)
- `timestamp` - ISO-8601 timestamp (automatically set to Instant.now())

**Key Points:**
- No `success` boolean field (HTTP status indicates success)
- Timestamp always in UTC using `Instant`
- Generic `<T>` for type-safe data handling

### Error Response Structure

All error responses follow this standardized format:

```json
{
  "status": 400,
  "message": "User registration failed",
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "type": "BUSINESS_LOGIC_ERROR",
    "details": "User with external ID 'auth0|123' already exists"
  },
  "path": "/users/signup",
  "method": "POST",
  "fieldErrors": [],
  "timestamp": "2023-12-25T10:15:30.123Z"
}
```

**Properties:**
- `status` - HTTP status code
- `message` - Human-readable error message
- `error` - Nested error details object
  - `code` - Machine-readable error code
  - `type` - Error category (from ErrorType enum)
  - `details` - Technical error details
- `path` - Request URI that caused the error
- `method` - HTTP method (GET, POST, etc.)
- `fieldErrors` - Array of field-level validation errors
- `timestamp` - ISO-8601 timestamp

### Error Response Builder

**Not typically used directly** - ErrorResponseBuilder exists but exception handlers usually construct ErrorResponse directly using setters for better control.

## Exception Handling System

### Architecture Overview

The exception handling system follows the **Strategy Pattern**:

1. **Exception with Metadata** - Domain exceptions carry context data
2. **Exception Handler** - Dedicated handler generates error responses
3. **Global Handler with Manual Mapping** - Maps exceptions to handlers

### Error Types

```java
public enum ErrorType {
    VALIDATION_ERROR,           // Input validation failures
    BUSINESS_LOGIC_ERROR,       // Domain rule violations
    AUTHENTICATION_ERROR,       // Authentication failures
    AUTHORIZATION_ERROR,        // Permission denied
    RESOURCE_NOT_FOUND,         // Entity not found
    CLIENT_ERROR,               // General client-side errors
    SERVER_ERROR                // Internal server errors
}
```

**Design Decisions:**
- Simple enum without additional properties or methods
- No exposure of internal system details (no DATABASE_ERROR, CONFIGURATION_ERROR, etc.)
- Focus on actionable error categories for API consumers

### Creating a New Exception

**Step 1: Create Exception Class (in domain package)**

```java
package dev.ganeshpalankar.rentals_backend.users.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String externalId;

    public UserAlreadyExistsException(String externalId) {
        super();  // No message - handler generates it
        this.externalId = externalId;
    }
}
```

**Key Points:**
- Extends `RuntimeException`
- Contains only metadata fields (no error messages)
- Uses `super()` with no message - handler creates user-facing message
- Uses Lombok `@Getter` for field access

**Step 2: Create Exception Handler (in domain package)**

```java
package dev.ganeshpalankar.rentals_backend.users.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.exception.ExceptionResponseHandler;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;

@Component
public class UserAlreadyExistsExceptionHandler implements ExceptionResponseHandler<UserAlreadyExistsException> {

    @Override
    public ErrorResponse handle(UserAlreadyExistsException exception, HttpServletRequest request) {
        // Build error detail
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("USER_ALREADY_EXISTS");
        errorDetail.setType(ErrorType.BUSINESS_LOGIC_ERROR.toString());
        errorDetail.setDetails(String.format("User with external ID '%s' already exists", exception.getExternalId()));

        // Build error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage("User registration failed");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}
```

**Key Points:**
- Implements `ExceptionResponseHandler<T>` interface
- Annotated with `@Component` for Spring auto-detection
- Returns `ErrorResponse` (not `ResponseEntity`)
- Uses exception metadata to create detailed error messages
- GlobalExceptionHandler wraps this in ResponseEntity

**Step 3: Register in GlobalExceptionHandler**

```java
private void initializeHandlers() {
    handlerMap.put(UserAlreadyExistsException.class, new UserAlreadyExistsExceptionHandler());
    // Add new mappings here
}
```

**Important:** Manual registration required in GlobalExceptionHandler's `initializeHandlers()` method.

### Throwing Exceptions

```java
// In service layer
if (userRepository.existsByExternalId(externalId)) {
    throw new UserAlreadyExistsException(externalId);
}
```

**Flow:**
1. Service throws exception with metadata
2. Spring catches exception (no try-catch in controller)
3. GlobalExceptionHandler looks up handler in map
4. Handler creates ErrorResponse with specific details
5. GlobalExceptionHandler wraps in ResponseEntity with status code
6. Client receives standardized JSON error

### Design Decisions

**Why no base exception class?**
- Each exception is independent with its own metadata
- No forced inheritance hierarchy
- Simpler and more flexible

**Why manual handler mapping?**
- Explicit and easy to understand
- No reflection complexity
- Clear view of all exception→handler mappings in one place
- Easy to debug and maintain

**Why separate exception and handler?**
- **Separation of concerns**: Business logic (exception) separate from HTTP concerns (handler)
- **Flexibility**: Same exception can have different handlers in different contexts
- **Testability**: Can test handlers independently
- **Metadata focus**: Exceptions carry data, handlers format responses

**Why handlers return ErrorResponse instead of ResponseEntity?**
- Handlers focus on content, not HTTP protocol
- GlobalExceptionHandler manages HTTP status codes and ResponseEntity creation
- Cleaner separation of responsibilities

## Integration with Controllers

Controllers should **not** handle exceptions directly:

```java
// ✅ GOOD - Let global handler catch exceptions
@PostMapping("/signup")
public ResponseEntity<ApiResponse<User>> signup(Authentication auth) {
    String externalId = extractExternalIdFromJwt(auth);
    User user = userService.signup(externalId);  // May throw exception
    return ResponseBuilder.<User>create()
        .status(HttpStatus.CREATED)
        .message("User created successfully")
        .data(user)
        .build();
}
```

## Validation Errors (Future Enhancement)

For field-level validation errors (e.g., from `@Valid` annotations), implement a separate handler:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
    // Extract field errors and populate fieldErrors array
}
```

## Best Practices

1. **Exceptions carry metadata only** - No error messages in exceptions
2. **Handlers create user-facing messages** - Use exception metadata to generate detailed messages
3. **One handler per exception** - Each exception type has dedicated handler
4. **Manual registration** - Explicitly register handlers in GlobalExceptionHandler
5. **No try-catch in controllers** - Let global handler manage all exceptions
6. **Use ErrorType enum** - Don't expose internal system details
7. **Timestamps always UTC** - Use `Instant.now()` for consistent timezone handling
8. **HTTP status in response body** - Matches the ResponseEntity status for API clarity

## Example: Complete Exception Handling Flow

```java
// 1. Service throws exception
public User signup(String externalId) {
    if (userRepository.existsByExternalId(externalId)) {
        throw new UserAlreadyExistsException(externalId);
    }
    // ... create user
}

// 2. Controller doesn't catch - exception propagates
@PostMapping("/signup")
public ResponseEntity<ApiResponse<User>> signup(Authentication auth) {
    User user = userService.signup(externalId);  // Exception thrown here
    return ResponseBuilder.<User>create()...;     // Never reached if exception thrown
}

// 3. GlobalExceptionHandler catches and delegates
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
    ExceptionResponseHandler handler = handlerMap.get(ex.getClass());
    ErrorResponse errorResponse = handler.handle(ex, request);
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
}

// 4. Specific handler creates response
public ErrorResponse handle(UserAlreadyExistsException exception, HttpServletRequest request) {
    // Build and return ErrorResponse with exception.getExternalId()
}

// 5. Client receives standardized error
{
  "status": 400,
  "message": "User registration failed",
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "type": "BUSINESS_LOGIC_ERROR",
    "details": "User with external ID 'auth0|123' already exists"
  },
  ...
}
```

## References

- Spring Boot `@ControllerAdvice` and `@ExceptionHandler` documentation
- RFC 7807: Problem Details for HTTP APIs (inspiration for error format)
- Leading API error response patterns (GitHub, Stripe, Google APIs)