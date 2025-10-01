package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for handling specific exception types and converting them to standardized error responses.
 * Each exception type should have its own implementation of this interface.
 *
 * @param <T> the type of exception this handler can process
 */
public interface ExceptionResponseHandler<T extends Exception> {

    /**
     * Handle the specific exception and generate an appropriate error response.
     * The GlobalExceptionHandler will wrap this in a ResponseEntity with the appropriate HTTP status.
     *
     * @param exception the exception that was thrown
     * @param request the HTTP request that caused the exception
     * @return the standardized error response object
     */
    ErrorResponse handle(T exception, HttpServletRequest request);
}