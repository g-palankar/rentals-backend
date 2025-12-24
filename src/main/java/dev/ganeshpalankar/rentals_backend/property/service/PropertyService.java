package dev.ganeshpalankar.rentals_backend.property.service;

import dev.ganeshpalankar.rentals_backend.property.dto.CreatePropertyRequest;
import dev.ganeshpalankar.rentals_backend.property.model.Property;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyType;
import dev.ganeshpalankar.rentals_backend.property.repository.PropertyRepository;
import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredException;
import dev.ganeshpalankar.rentals_backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for property management operations.
 */
@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Transactional
    public Property createProperty(CreatePropertyRequest request, Long ownerId) {
        Property property = new Property();
        property.setOwnerId(ownerId);
        property.setPropertyName(request.getPropertyName());
        property.setAddress(request.getAddress());
        property.setPropertyType(PropertyType.valueOf(request.getPropertyType()));
        property.setCreatedBy(ownerId);

        return propertyRepository.save(property);
    }

    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElse(null);
    }
}
