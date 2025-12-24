package dev.ganeshpalankar.rentals_backend.property.mapper;

import dev.ganeshpalankar.rentals_backend.property.dto.PropertyResponse;
import dev.ganeshpalankar.rentals_backend.property.model.Property;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Property entity and DTOs.
 */
@Component
public class PropertyMapper {

    /**
     * Converts a Property entity to PropertyResponse DTO.
     *
     * @param property the property entity
     * @return PropertyResponse DTO
     */
    public PropertyResponse toResponse(Property property) {
        if (property == null) {
            return null;
        }

        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());
        response.setPropertyName(property.getPropertyName());
        response.setAddress(property.getAddress());
        response.setPropertyType(property.getPropertyType().name());
        response.setOwnerId(property.getOwnerId());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());

        return response;
    }
}
