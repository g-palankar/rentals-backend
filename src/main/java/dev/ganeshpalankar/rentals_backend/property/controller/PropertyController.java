package dev.ganeshpalankar.rentals_backend.property.controller;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ResponseBuilder;
import dev.ganeshpalankar.rentals_backend.property.dto.CreatePropertyRequest;
import dev.ganeshpalankar.rentals_backend.property.dto.PropertyResponse;
import dev.ganeshpalankar.rentals_backend.property.mapper.PropertyMapper;
import dev.ganeshpalankar.rentals_backend.property.model.Property;
import dev.ganeshpalankar.rentals_backend.property.service.PropertyService;
import dev.ganeshpalankar.rentals_backend.users.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for property management operations.
 */
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final UserContextService userContextService;
    private final PropertyMapper propertyMapper;

    /**
     * Create a new property for the currently authenticated user.
     *
     * @param request the property creation request
     * @return ResponseEntity containing the created property
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(
            @Valid @RequestBody CreatePropertyRequest request) {

        Long currentUserId = userContextService.getCurrentUserId();

        Property property = propertyService.createProperty(request, currentUserId);

        PropertyResponse response = propertyMapper.toResponse(property);

        return ResponseBuilder.<PropertyResponse>create()
                .status(HttpStatus.CREATED)
                .message("Property created successfully")
                .data(response)
                .build();
    }
}
