package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.common.response.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exception handler for validation-related exceptions.
 * Handles @Valid and @Validated validation errors.
 */
@ControllerAdvice
public class ValidationExceptionHandler {

    /**
     * Handle bean validation errors from @Valid annotation on request body.
     * Collects all field errors and returns them in a single response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Group field errors by field name
        List<FieldError> fieldErrors = groupFieldErrors(ex.getBindingResult().getFieldErrors());

        // Build error detail
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("VALIDATION_FAILED");
        errorDetail.setType(ErrorType.VALIDATION_ERROR.toString());
        errorDetail.setDetails(String.format("%d validation error(s) occurred", fieldErrors.size()));

        // Build error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage("Validation failed");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setTimestamp(Instant.now());

        return ResponseEntity.status(400).body(errorResponse);
    }

    /**
     * Handle constraint violations from @Validated on path/query parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        // Group constraint violations by field name
        List<FieldError> fieldErrors = groupConstraintViolations(ex.getConstraintViolations());

        // Build error detail
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("VALIDATION_FAILED");
        errorDetail.setType(ErrorType.VALIDATION_ERROR.toString());
        errorDetail.setDetails(String.format("%d validation error(s) occurred", fieldErrors.size()));

        // Build error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage("Validation failed");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setTimestamp(Instant.now());

        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ){
        // Build error detail
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("INVALID_PARAMETER_TYPE");
        errorDetail.setType(ErrorType.VALIDATION_ERROR.toString());

        // Build error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage(String.format(
                "Parameter '%s' must be a valid number. Received: '%s'",
                ex.getName(),
                ex.getValue()
        ));
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setTimestamp(Instant.now());

        return ResponseEntity.status(400).body(errorResponse);
    }

    /**
     * Handle JSON parsing errors (malformed JSON, invalid enum values, type mismatches).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        // Extract useful error message
        String details = ex.getMessage();
        if (details != null && details.contains(":")) {
            details = details.substring(0, details.indexOf(":"));
        }

        // Build error detail
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("INVALID_REQUEST_BODY");
        errorDetail.setType(ErrorType.VALIDATION_ERROR.toString());
        errorDetail.setDetails(details != null ? details : "Invalid request body format");

        // Build error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage("Invalid request body");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return ResponseEntity.status(400).body(errorResponse);
    }


    // --- HELPER METHODS ---

    /**
     * Group field errors by field name and merge their messages.
     * Multiple validation errors for the same field are combined into a single FieldError
     * with multiple messages.
     *
     * @param springFieldErrors List of Spring's field errors
     * @return List of grouped FieldErrors with merged messages
     */
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

    /**
     * Group constraint violations by property path and merge their messages.
     * Multiple constraint violations for the same property are combined into a single FieldError
     * with multiple messages.
     *
     * @param violations Set of constraint violations
     * @return List of grouped FieldErrors with merged messages
     */
    private List<FieldError> groupConstraintViolations(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .collect(Collectors.groupingBy(
                        violation -> violation.getPropertyPath().toString(),
                        LinkedHashMap::new,  // Preserve order of first occurrence
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    String fieldName = entry.getKey();
                    List<ConstraintViolation<?>> violationList = entry.getValue();

                    // Collect all messages for this field
                    List<String> messages = violationList.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toList());

                    // Use the invalid value from the first violation
                    Object rejectedValue = violationList.getFirst().getInvalidValue();

                    FieldError fieldError = new FieldError();
                    fieldError.setField(fieldName);
                    fieldError.setMessages(messages);
                    fieldError.setRejectedValue(rejectedValue);
                    return fieldError;
                })
                .collect(Collectors.toList());
    }
}
