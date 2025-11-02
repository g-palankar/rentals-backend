package dev.ganeshpalankar.rentals_backend.users.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.exception.ExceptionResponseHandler;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Handler for UserNotRegisteredException that generates appropriate error responses.
 */
@Component
public class UserNotRegisteredExceptionHandler implements ExceptionResponseHandler<UserNotRegisteredException> {

    @Override
    public ErrorResponse handle(UserNotRegisteredException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("USER_NOT_REGISTERED");
        errorDetail.setType(ErrorType.AUTHENTICATION_ERROR.toString());
        errorDetail.setDetails(String.format("User with external ID '%s' is not registered. Please complete signup.", exception.getExternalId()));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(401);
        errorResponse.setMessage("User not registered");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}
