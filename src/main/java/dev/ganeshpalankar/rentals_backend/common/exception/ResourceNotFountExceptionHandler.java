package dev.ganeshpalankar.rentals_backend.common.exception;

import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Component
public class ResourceNotFountExceptionHandler implements ExceptionResponseHandler<ResourceNotFoundException> {

    @Override
    public ErrorResponse handle(ResourceNotFoundException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("RESOURCE_NOT_FOUND");
        errorDetail.setType(ErrorType.RESOURCE_NOT_FOUND.toString());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(404);
        errorResponse.setMessage(String.format(
                "%s with ID '%s' does not exist",
                exception.getResourceName(),
                exception.getResourceId()
        ));
        errorResponse.setError(errorDetail);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        errorResponse.setFieldErrors(new ArrayList<>());
        errorResponse.setTimestamp(Instant.now());

        return errorResponse;
    }
}
