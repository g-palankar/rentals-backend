package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.exception.ExceptionResponseHandler;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.ArrayList;

public class MemberAlreadyExistsExceptionHandler implements ExceptionResponseHandler<MemberAlreadyExistsException> {

    @Override
    public ErrorResponse handle(MemberAlreadyExistsException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("MEMBER_ALREADY_EXISTS");
        errorDetail.setType(ErrorType.BUSINESS_LOGIC_ERROR.toString());
        errorDetail.setDetails(String.format(
                "User '%s' is already a member of organisation '%s'",
                exception.getUserId(), exception.getOrganisationId()));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(409);
        errorResponse.setMessage("User is already a member of this organisation");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}
