package dev.ganeshpalankar.rentals_backend.organisation.exception;

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
public class OrganisationExceptionHandler {

    @ExceptionHandler(OrganisationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrganisationNotFound(OrganisationNotFoundException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(404)
                .message("Organisation not found")
                .errorCode("ORGANISATION_NOT_FOUND")
                .errorType(ErrorType.RESOURCE_NOT_FOUND.toString())
                .errorDetails("Organisation with ID '%s' does not exist".formatted(ex.getOrganisationId()))
                .request(request)
                .build();
    }

    @ExceptionHandler(LastAdminException.class)
    public ResponseEntity<ErrorResponse> handleLastAdmin(LastAdminException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(409)
                .message("Cannot remove or demote the last admin of an organisation")
                .errorCode("LAST_ADMIN")
                .errorType(ErrorType.BUSINESS_LOGIC_ERROR.toString())
                .errorDetails("Organisation '%s' must retain at least one admin".formatted(ex.getOrganisationId()))
                .request(request)
                .build();
    }

    @ExceptionHandler(OrgHasPropertiesException.class)
    public ResponseEntity<ErrorResponse> handleOrgHasProperties(OrgHasPropertiesException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(409)
                .message("Cannot delete an organisation that has properties")
                .errorCode("ORG_HAS_PROPERTIES")
                .errorType(ErrorType.BUSINESS_LOGIC_ERROR.toString())
                .errorDetails("Organisation '%s' still has properties linked to it".formatted(ex.getOrganisationId()))
                .request(request)
                .build();
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMemberAlreadyExists(MemberAlreadyExistsException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(409)
                .message("User is already a member of this organisation")
                .errorCode("MEMBER_ALREADY_EXISTS")
                .errorType(ErrorType.BUSINESS_LOGIC_ERROR.toString())
                .errorDetails("User '%s' is already a member of organisation '%s'".formatted(ex.getUserId(), ex.getOrganisationId()))
                .request(request)
                .build();
    }

    @ExceptionHandler(UserNotFoundByEmailException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundByEmail(UserNotFoundByEmailException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.create()
                .status(404)
                .message("User not found")
                .errorCode("USER_NOT_FOUND")
                .errorType(ErrorType.RESOURCE_NOT_FOUND.toString())
                .errorDetails("No registered user found with email '%s'".formatted(ex.getEmail()))
                .request(request)
                .build();
    }
}
