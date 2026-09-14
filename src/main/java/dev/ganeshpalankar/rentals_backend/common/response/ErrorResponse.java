package dev.ganeshpalankar.rentals_backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard error response envelope")
public class ErrorResponse {
    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Human-readable error message", example = "Organisation not found")
    private String message;

    @Schema(description = "Structured error details")
    private ErrorDetail error;

    @Schema(description = "Request URI that caused the error", example = "/api/organisations/42")
    private String path;

    @Schema(description = "HTTP method of the failed request", example = "GET")
    private String method;

    @Schema(description = "Field-level validation errors; null or empty for non-validation errors")
    private List<FieldError> fieldErrors;

    @Schema(description = "UTC timestamp of when the error occurred", example = "2024-06-01T12:00:00Z")
    private Instant timestamp;
}