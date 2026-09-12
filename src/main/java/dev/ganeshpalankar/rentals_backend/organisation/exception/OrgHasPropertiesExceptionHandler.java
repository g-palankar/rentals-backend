package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.exception.ExceptionResponseHandler;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.ArrayList;

public class OrgHasPropertiesExceptionHandler implements ExceptionResponseHandler<OrgHasPropertiesException> {

    @Override
    public ErrorResponse handle(OrgHasPropertiesException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("ORG_HAS_PROPERTIES");
        errorDetail.setType(ErrorType.BUSINESS_LOGIC_ERROR.toString());
        errorDetail.setDetails(String.format(
                "Organisation '%s' still has properties linked to it", exception.getOrganisationId()));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(409);
        errorResponse.setMessage("Cannot delete an organisation that has properties");
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}
