package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.ArrayList;

public class ForbiddenExceptionHandler implements ExceptionResponseHandler<ForbiddenException> {

    @Override
    public ErrorResponse handle(ForbiddenException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("FORBIDDEN");
        errorDetail.setType(ErrorType.AUTHORIZATION_ERROR.toString());
        errorDetail.setDetails(exception.getReason());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(403);
        errorResponse.setMessage("Access denied");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}