package dev.ganeshpalankar.rentals_backend.users.exception;

import lombok.Getter;

/**
 * Exception thrown when attempting to create a user that already exists.
 * Contains metadata about the user identifier for detailed error reporting.
 */
@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private final String externalId;

    /**
     * Create a new UserAlreadyExistsException.
     *
     * @param externalId the external ID that already exists
     */
    public UserAlreadyExistsException(String externalId) {
        super();
        this.externalId = externalId;
    }
}