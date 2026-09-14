package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class CommonExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(403)
                .message("Access denied")
                .errorCode("FORBIDDEN")
                .errorType(ErrorType.AUTHORIZATION_ERROR.toString())
                .errorDetails(ex.getReason())
                .request(request)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(404)
                .message("%s with ID '%s' does not exist".formatted(ex.getResourceName(), ex.getResourceId()))
                .errorCode("RESOURCE_NOT_FOUND")
                .errorType(ErrorType.RESOURCE_NOT_FOUND.toString())
                .request(request)
                .build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleUnmappedException(ApplicationException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(500)
                .message("Internal server error")
                .errorCode("INTERNAL_ERROR")
                .errorType(ErrorType.SERVER_ERROR.toString())
                .errorDetails("An unexpected error occurred")
                .request(request)
                .build();
    }
}
