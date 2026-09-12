package dev.ganeshpalankar.rentals_backend.organisation.exception;

import dev.ganeshpalankar.rentals_backend.common.exception.ApplicationException;
import lombok.Getter;

@Getter
public class MemberAlreadyExistsException extends ApplicationException {
    private final Long organisationId;
    private final Long userId;

    public MemberAlreadyExistsException(Long organisationId, Long userId) {
        super();
        this.organisationId = organisationId;
        this.userId = userId;
    }
}
