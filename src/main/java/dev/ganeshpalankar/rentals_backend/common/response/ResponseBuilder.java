package dev.ganeshpalankar.rentals_backend.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseBuilder<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;

    private ResponseBuilder() {}

    public static <T> ResponseBuilder<T> create() {
        return new ResponseBuilder<T>();
    }

    public ResponseBuilder<T> status(HttpStatus status) {
        this.httpStatus = status;
        return this;
    }

    public ResponseBuilder<T> status(int statusCode) {
        this.httpStatus = HttpStatus.valueOf(statusCode);
        return this;
    }

    public ResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResponseEntity<ApiResponse<T>> build() {
        if (this.httpStatus == null) {
            throw new IllegalStateException("HTTP status is required");
        }
        if (this.message == null) {
            throw new IllegalStateException("Message is required");
        }

        ApiResponse<T> apiResponse = new ApiResponse<>(
            this.httpStatus.value(),
            this.message,
            this.data,
            Instant.now()
        );

        return ResponseEntity
            .status(this.httpStatus)
            .body(apiResponse);
    }
}