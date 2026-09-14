package dev.ganeshpalankar.rentals_backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard success response envelope")
public class ApiResponse<T> {
    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Human-readable result message", example = "Organisation retrieved successfully")
    private String message;

    @Schema(description = "Response payload")
    private T data;

    @Schema(description = "UTC timestamp of when the response was generated", example = "2024-06-01T12:00:00Z")
    private Instant timestamp;
}