package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class OrgHasPropertiesException extends ApplicationException {
    private final Long organisationId;

    public OrgHasPropertiesException(Long organisationId) {
        super();
        this.organisationId = organisationId;
    }
}
