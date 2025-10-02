package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler with manual mapping of exceptions to their handlers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends Exception>, ExceptionResponseHandler<?>> handlerMap;

    public GlobalExceptionHandler() {
        this.handlerMap = new HashMap<>();
        initializeHandlers();
    }

    private void initializeHandlers() {
        // Manual mapping of exceptions to their handlers
//        handlerMap.put(UserAlreadyExistsException.class, new UserAlreadyExistsExceptionHandler());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ExceptionResponseHandler<Exception> handler = (ExceptionResponseHandler<Exception>) handlerMap.get(ex.getClass());

        if (handler != null) {
            ErrorResponse errorResponse = handler.handle(ex, request);
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // Fallback for unmapped exceptions
        return handleGenericException(ex, request);
    }

    private ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("INTERNAL_ERROR");
        errorDetail.setType(ErrorType.SERVER_ERROR.toString());
        errorDetail.setDetails("An unexpected error occurred");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setMessage("Internal server error");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return ResponseEntity.status(500).body(errorResponse);
    }
}
