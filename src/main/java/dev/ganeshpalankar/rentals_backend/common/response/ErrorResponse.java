package dev.ganeshpalankar.rentals_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private ErrorDetail error;
    private String path;
    private String method;
    private List<FieldError> fieldErrors;
    private Instant timestamp;
}