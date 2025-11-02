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
│   └── FieldError.java            # Field-level validation errors (supports multiple messages)
├── exception/
│   ├── ApplicationException.java  # Base class for all application exceptions
│   ├── ErrorType.java             # Error type enumeration
│   ├── ExceptionResponseHandler.java  # Handler interface
│   ├── GlobalExceptionHandler.java    # Application exception handling
│   └── ValidationExceptionHandler.java # Framework validation exception handling
└── validation/
    ├── ValidEnum.java             # Custom enum validation annotation
    └── EnumValidator.java         # Enum validator implementation
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
  "message": "Validation failed",
  "error": {
    "code": "VALIDATION_FAILED",
    "type": "VALIDATION_ERROR",
    "details": "2 validation error(s) occurred"
  },
  "path": "/api/properties",
  "method": "POST",
  "fieldErrors": [
    {
      "field": "propertyType",
      "messages": ["Property type is required", "Invalid property type. Allowed values: APARTMENT, HOUSE, COMMERCIAL, VILLA"],
      "rejectedValue": null
    },
    {
      "field": "address",
      "messages": ["Address is required"],
      "rejectedValue": ""
    }
  ],
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
  - `field` - Name of the field with validation error
  - `messages` - Array of validation messages (multiple errors grouped per field)
  - `rejectedValue` - The invalid value that was submitted
- `timestamp` - ISO-8601 timestamp

### Error Response Builder

**Not typically used directly** - ErrorResponseBuilder exists but exception handlers usually construct ErrorResponse directly using setters for better control.

## Exception Handling System

### Architecture Overview

The exception handling system uses **two separate handlers** for different exception types:

1. **GlobalExceptionHandler** - Handles application-specific exceptions that extend `ApplicationException`
   - Uses Strategy Pattern with manual exception-to-handler mapping
   - Only catches exceptions extending ApplicationException base class
   - Delegates to specific handlers for detailed error responses

2. **ValidationExceptionHandler** - Handles framework validation exceptions
   - Catches `MethodArgumentNotValidException` (@Valid on request body)
   - Catches `ConstraintViolationException` (@Validated on parameters)
   - Catches `HttpMessageNotReadableException` (JSON parsing errors)
   - Groups multiple validation errors per field into single entries

**Key Design Principle:** Separation between application domain exceptions and framework validation exceptions.

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

### ApplicationException Base Class

**All application-specific exceptions MUST extend ApplicationException:**

```java
package dev.ganeshpalankar.rentals_backend.common.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
```

**Why ApplicationException?**
- Ensures GlobalExceptionHandler only catches application domain exceptions
- Prevents catching framework exceptions (validation, security, etc.)
- Allows ValidationExceptionHandler to handle framework validation exceptions separately
- Clear separation of concerns between business logic errors and validation errors

### Creating a New Application Exception

**Step 1: Create Exception Class (in domain package)**

```java
package dev.ganeshpalankar.rentals_backend.users.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends ApplicationException {
    private final String externalId;

    public UserAlreadyExistsException(String externalId) {
        super();  // No message - handler generates it
        this.externalId = externalId;
    }
}
```

**Key Points:**
- **MUST extend `ApplicationException`** (not RuntimeException directly)
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

**Why ApplicationException base class?**
- Ensures GlobalExceptionHandler only catches application exceptions
- Prevents catching framework validation or security exceptions
- Enables separate handling of validation vs business logic errors
- Clear separation between domain concerns and framework concerns

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

## Validation Exception Handling

### ValidationExceptionHandler

The `ValidationExceptionHandler` handles all framework validation exceptions separately from application exceptions:

```java
@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Group field errors by field name
        List<FieldError> fieldErrors = groupFieldErrors(ex.getBindingResult().getFieldErrors());

        // Build error response...
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        // Group constraint violations by property path
        List<FieldError> fieldErrors = groupConstraintViolations(ex.getConstraintViolations());

        // Build error response...
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        // Handle JSON parsing errors (malformed JSON, invalid enum values, type mismatches)
    }
}
```

### Field Error Grouping

**Key Feature:** Multiple validation errors for the same field are grouped into a single `FieldError` entry with multiple messages.

**Grouping Logic:**
```java
private List<FieldError> groupFieldErrors(List<org.springframework.validation.FieldError> springFieldErrors) {
    return springFieldErrors.stream()
            .collect(Collectors.groupingBy(
                    org.springframework.validation.FieldError::getField,
                    LinkedHashMap::new,  // Preserve order of first occurrence
                    Collectors.toList()
            ))
            .entrySet()
            .stream()
            .map(entry -> {
                String fieldName = entry.getKey();
                List<org.springframework.validation.FieldError> errors = entry.getValue();

                // Collect all messages for this field
                List<String> messages = errors.stream()
                        .map(org.springframework.validation.FieldError::getDefaultMessage)
                        .collect(Collectors.toList());

                // Use the rejected value from the first error
                Object rejectedValue = errors.get(0).getRejectedValue();

                FieldError fieldError = new FieldError();
                fieldError.setField(fieldName);
                fieldError.setMessages(messages);
                fieldError.setRejectedValue(rejectedValue);
                return fieldError;
            })
            .collect(Collectors.toList());
}
```

**Benefits:**
- Cleaner error responses without duplicate field names
- All validation messages for a field grouped together
- Preserves order of first occurrence using `LinkedHashMap`
- Single `rejectedValue` per field (from first error)

**Example Response:**
```json
{
  "fieldErrors": [
    {
      "field": "propertyType",
      "messages": [
        "Property type is required",
        "Invalid property type. Allowed values: APARTMENT, HOUSE, COMMERCIAL, VILLA"
      ],
      "rejectedValue": null
    }
  ]
}
```

### Custom Enum Validation

**Problem:** Invalid enum values in JSON cause `HttpMessageNotReadableException` with unclear error messages.

**Solution:** Custom `@ValidEnum` annotation with validator.

**Step 1: Create Annotation**
```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Documented
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value for enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

**Step 2: Create Validator**
```java
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        // Compare string value against enum constant names
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
    }
}
```

**Current Implementation Note:**
The actual code in the repository uses `e.equals(value)` which compares an enum constant to a String. This will always return `false` since an enum cannot equal a String. The correct implementation should use `e.name().equals(value)` to compare the enum constant's name with the string value.

**Step 3: Apply to DTO Field**
```java
@Getter
@Setter
public class CreatePropertyRequest {
    @NotNull(message = "Property type is required")
    @ValidEnum(enumClass = PropertyType.class,
               message = "Invalid property type. Allowed values: APARTMENT, HOUSE, COMMERCIAL, VILLA")
    private String propertyType;  // Note: String type, not PropertyType enum
}
```

**How It Works:**
1. Field is declared as `String` type (not enum type)
2. Jackson deserializes the JSON string value directly
3. `@NotNull` validator ensures the value is not null
4. `@ValidEnum` validator checks if the string matches a valid enum constant name using `e.name().equals(value)`
5. If validation fails, both errors can be grouped into single FieldError with multiple messages

**Benefits:**
- User-friendly error messages for invalid enum values
- Simple string comparison against enum constant names
- No need to parse `HttpMessageNotReadableException` messages
- Works seamlessly with field error grouping
- Reusable across all enum fields that need string-based validation

**Note:** The field must be `String` type for this validator to work. If you need to work with actual enum types in your service layer, convert the validated string to the enum after validation passes.

## Best Practices

### Application Exceptions
1. **All application exceptions MUST extend ApplicationException** - Required for GlobalExceptionHandler to catch them
2. **Exceptions carry metadata only** - No error messages in exceptions
3. **Handlers create user-facing messages** - Use exception metadata to generate detailed messages
4. **One handler per exception** - Each exception type has dedicated handler
5. **Manual registration** - Explicitly register handlers in GlobalExceptionHandler
6. **No try-catch in controllers** - Let global handler manage all exceptions

### Validation
7. **Use @ValidEnum for enum fields** - Provides user-friendly error messages for invalid enum values
8. **Field must be String type** - EnumValidator validates String fields against enum constant names
9. **Combine @NotNull with @ValidEnum** - Both validators work together via field error grouping
10. **Custom validators for complex rules** - Create reusable validators like @ValidEnum

### General
11. **Use ErrorType enum** - Don't expose internal system details
12. **Timestamps always UTC** - Use `Instant.now()` for consistent timezone handling
13. **HTTP status in response body** - Matches the ResponseEntity status for API clarity
14. **Field errors always grouped** - Multiple errors per field appear as array of messages

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