package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.organisation.exception.LastAdminException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.LastAdminExceptionHandler;
import dev.ganeshpalankar.rentals_backend.organisation.exception.MemberAlreadyExistsException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.MemberAlreadyExistsExceptionHandler;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrgHasPropertiesException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrgHasPropertiesExceptionHandler;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrganisationNotFoundException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrganisationNotFoundExceptionHandler;
import dev.ganeshpalankar.rentals_backend.organisation.exception.UserNotFoundByEmailException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.UserNotFoundByEmailExceptionHandler;
import dev.ganeshpalankar.rentals_backend.users.exception.UserAlreadyExistsException;
import dev.ganeshpalankar.rentals_backend.users.exception.UserAlreadyExistsExceptionHandler;
import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredException;
import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredExceptionHandler;
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
 * Only handles application-specific exceptions that extend ApplicationException.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends ApplicationException>, ExceptionResponseHandler<?>> handlerMap;

    public GlobalExceptionHandler() {
        this.handlerMap = new HashMap<>();
        initializeHandlers();
    }

    private void initializeHandlers() {
        handlerMap.put(UserAlreadyExistsException.class, new UserAlreadyExistsExceptionHandler());
        handlerMap.put(UserNotRegisteredException.class, new UserNotRegisteredExceptionHandler());
        handlerMap.put(ResourceNotFoundException.class, new ResourceNotFoundExceptionHandler());
        handlerMap.put(ForbiddenException.class, new ForbiddenExceptionHandler());
        handlerMap.put(OrganisationNotFoundException.class, new OrganisationNotFoundExceptionHandler());
        handlerMap.put(LastAdminException.class, new LastAdminExceptionHandler());
        handlerMap.put(OrgHasPropertiesException.class, new OrgHasPropertiesExceptionHandler());
        handlerMap.put(UserNotFoundByEmailException.class, new UserNotFoundByEmailExceptionHandler());
        handlerMap.put(MemberAlreadyExistsException.class, new MemberAlreadyExistsExceptionHandler());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException ex, HttpServletRequest request) {
        ExceptionResponseHandler<ApplicationException> handler = (ExceptionResponseHandler<ApplicationException>) handlerMap.get(ex.getClass());

        if (handler != null) {
            ErrorResponse errorResponse = handler.handle(ex, request);
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // Fallback for unmapped application exceptions
        return handleGenericException(ex, request);
    }

    private ResponseEntity<ErrorResponse> handleGenericException(ApplicationException ex, HttpServletRequest request) {
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
