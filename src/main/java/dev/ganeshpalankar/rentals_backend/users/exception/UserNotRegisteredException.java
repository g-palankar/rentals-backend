package dev.ganeshpalankar.rentals_backend.users.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

/**
 * Exception thrown when a user is not registered in the system.
 * Contains metadata about the user identifier for detailed error reporting.
 */
@Getter
public class UserNotRegisteredException extends ApplicationException {
    private final String externalId;

    public UserNotRegisteredException(String externalId) {
        super();
        this.externalId = externalId;
    }
}
