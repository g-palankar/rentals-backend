package dev.ganeshpalankar.rentals_backend.property.dto;

import dev.ganeshpalankar.rentals_backend.common.validation.ValidEnum;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for creating a new property.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyRequest {
    @NotBlank(message = "Property name is required")
    private String propertyName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Property type is required")
    @ValidEnum(enumClass = PropertyType.class, message = "Invalid property type. Allowed values: APARTMENT, HOUSE, COMMERCIAL, VILLA")
    private String propertyType;
}

