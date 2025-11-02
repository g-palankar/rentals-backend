package dev.ganeshpalankar.rentals_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents field-level validation errors.
 * Multiple validation messages for the same field are grouped together.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldError {
    private String field;
    private List<String> messages;
    private Object rejectedValue;
}