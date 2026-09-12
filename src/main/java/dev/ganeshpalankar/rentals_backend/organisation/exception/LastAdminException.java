package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class LastAdminException extends ApplicationException {
    private final Long organisationId;

    public LastAdminException(Long organisationId) {
        super();
        this.organisationId = organisationId;
    }
}
