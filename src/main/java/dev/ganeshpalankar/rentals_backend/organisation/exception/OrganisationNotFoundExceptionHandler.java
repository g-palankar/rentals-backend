package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.exception.ExceptionResponseHandler;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.ArrayList;

public class OrganisationNotFoundExceptionHandler implements ExceptionResponseHandler<OrganisationNotFoundException> {

    @Override
    public ErrorResponse handle(OrganisationNotFoundException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("ORGANISATION_NOT_FOUND");
        errorDetail.setType(ErrorType.RESOURCE_NOT_FOUND.toString());
        errorDetail.setDetails(String.format("Organisation with ID '%s' does not exist", exception.getOrganisationId()));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(404);
        errorResponse.setMessage("Organisation not found");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}