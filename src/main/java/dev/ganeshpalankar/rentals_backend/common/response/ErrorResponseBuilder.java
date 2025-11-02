package dev.ganeshpalankar.rentals_backend.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponseBuilder {
    private Integer status;
    private String message;
    private ErrorDetail error;
    private String path;
    private String method;
    private List<FieldError> fieldErrors = new ArrayList<>();
    private Instant timestamp;

    private ErrorResponseBuilder() {}

    // Static factory method
    public static ErrorResponseBuilder create() {
        return new ErrorResponseBuilder();
    }

    // Instance methods for chaining
    public ErrorResponseBuilder status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponseBuilder status(HttpStatus httpStatus) {
        this.status = httpStatus.value();
        return this;
    }

    public ErrorResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseBuilder error(ErrorDetail error) {
        this.error = error;
        return this;
    }

    public ErrorResponseBuilder errorCode(String code) {
        if (this.error == null) {
            this.error = new ErrorDetail();
        }
        this.error.setCode(code);
        return this;
    }

    public ErrorResponseBuilder errorType(String type) {
        if (this.error == null) {
            this.error = new ErrorDetail();
        }
        this.error.setType(type);
        return this;
    }

    public ErrorResponseBuilder errorDetails(String details) {
        if (this.error == null) {
            this.error = new ErrorDetail();
        }
        this.error.setDetails(details);
        return this;
    }

    public ErrorResponseBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ErrorResponseBuilder method(String method) {
        this.method = method;
        return this;
    }

    public ErrorResponseBuilder fieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors != null ? fieldErrors : new ArrayList<>();
        return this;
    }

    public ErrorResponseBuilder addFieldError(String field, String message, Object rejectedValue) {
        FieldError fieldError = new FieldError();
        fieldError.setField(field);
        fieldError.setMessages(List.of(message));
        fieldError.setRejectedValue(rejectedValue);
        this.fieldErrors.add(fieldError);
        return this;
    }

    public ErrorResponseBuilder addFieldError(FieldError fieldError) {
        this.fieldErrors.add(fieldError);
        return this;
    }

    public ErrorResponseBuilder timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ResponseEntity<ErrorResponse> build() {
        // Validation
        if (status == null) {
            throw new IllegalStateException("Status is required");
        }

        // Auto-set timestamp if not provided
        if (timestamp == null) {
            timestamp = Instant.now();
        }

        ErrorResponse response = new ErrorResponse();
        response.setStatus(status);
        response.setMessage(message);
        response.setError(error);
        response.setPath(path);
        response.setMethod(method);
        response.setFieldErrors(fieldErrors);
        response.setTimestamp(timestamp);

        return ResponseEntity.status(status).body(response);
    }
}