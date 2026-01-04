package dev.ganeshpalankar.rentals_backend.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends ApplicationException {
    private final Long resourceId;
    private final String resourceName;

    public ResourceNotFoundException(Long resourceId, String resourceName) {
        super();
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }
}
