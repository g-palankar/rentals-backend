package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class OrganisationNotFoundException extends ApplicationException {
    private final Long organisationId;

    public OrganisationNotFoundException(Long organisationId) {
        super();
        this.organisationId = organisationId;
    }
}
