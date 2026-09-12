package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class UserNotFoundByEmailException extends ApplicationException {
    private final String email;

    public UserNotFoundByEmailException(String email) {
        super();
        this.email = email;
    }
}
