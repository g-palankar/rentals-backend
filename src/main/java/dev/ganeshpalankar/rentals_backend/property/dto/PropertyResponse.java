package dev.ganeshpalankar.rentals_backend.property.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Response DTO for property data.
 * Used when returning property information to clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private String propertyName;
    private String address;
    private String propertyType;
    private Long ownerId;
    private Instant createdAt;
    private Instant updatedAt;
}
