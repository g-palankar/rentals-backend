package dev.ganeshpalankar.rentals_backend.common.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends ApplicationException {
    private final String reason;

    public ForbiddenException(String reason) {
        super();
        this.reason = reason;
    }
}