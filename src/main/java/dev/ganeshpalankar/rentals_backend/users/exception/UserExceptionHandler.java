package dev.ganeshpalankar.rentals_backend.users.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Order(1)
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(400)
                .message("User registration failed")
                .errorCode("USER_ALREADY_EXISTS")
                .errorType(ErrorType.BUSINESS_LOGIC_ERROR.toString())
                .errorDetails("User with external ID '%s' already exists".formatted(ex.getExternalId()))
                .request(request)
                .build();
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleUserNotRegistered(UserNotRegisteredException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(403)
                .message("User not registered")
                .errorCode("USER_NOT_REGISTERED")
                .errorType(ErrorType.AUTHORIZATION_ERROR.toString())
                .errorDetails("User with external ID '%s' is not registered. Please complete signup.".formatted(ex.getExternalId()))
                .request(request)
                .build();
    }
}
