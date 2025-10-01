package dev.ganeshpalankar.rentals_backend.common.exception;

/**
 * Enumeration of error types for standardized error categorization.
 * These types help clients understand the nature of errors and how to handle them.
 */
public enum ErrorType {

    /**
     * Input validation failures, invalid request data
     */
    VALIDATION_ERROR,

    /**
     * Business rule violations, domain constraints
     */
    BUSINESS_LOGIC_ERROR,

    /**
     * Authentication required or failed
     */
    AUTHENTICATION_ERROR,

    /**
     * Permission denied, insufficient access
     */
    AUTHORIZATION_ERROR,

    /**
     * Requested resource not found
     */
    RESOURCE_NOT_FOUND,

    /**
     * General client-side error
     */
    CLIENT_ERROR,

    /**
     * Internal server error occurred
     */
    SERVER_ERROR
}