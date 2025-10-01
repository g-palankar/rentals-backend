package dev.ganeshpalankar.rentals_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;
}