package dev.ganeshpalankar.rentals_backend.property.service;

import dev.ganeshpalankar.rentals_backend.property.model.Property;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyPermission;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyRole;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyRoleAssignment;
import dev.ganeshpalankar.rentals_backend.property.repository.PropertyRepository;
import dev.ganeshpalankar.rentals_backend.property.repository.PropertyRoleAssignmentRepository;
import dev.ganeshpalankar.rentals_backend.users.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service for checking property-level permissions.
 * <p>
 * This service is called directly by @PreAuthorize expressions in controllers.
 * Bean name: propertyPermissionService
 * <p>
 * Example usage:
 * @PreAuthorize("@propertyPermissionService.hasPermission(#id, 'PROPERTY_READ')")
 */
@Service
@RequiredArgsConstructor
public class PropertyPermissionService {

    private final PropertyService propertyService;
    private final PropertyRoleAssignmentRepository roleAssignmentRepository;
    private final UserContextService userContextService;
    private final Map<PropertyRole, Set<PropertyPermission>> propertyRolePermissionMap;

    /**
     * Check if the current user has a specific permission for a property.
     * <p>
     * Permission is granted if:
     * 1. User is the property owner (implicit all permissions), OR
     * 2. User has a role that grants the requested permission
     *
     * @param propertyId the property ID
     * @param permissionName the permission name (e.g., "PROPERTY_READ")
     * @return true if user has permission, false otherwise
     */
    public boolean hasPermission(Long propertyId, String permissionName) {
        try {
            PropertyPermission permission = PropertyPermission.valueOf(permissionName);
            return hasPermission(propertyId, permission);
        } catch (IllegalArgumentException e) {
            return  false;
        }
    }

    /**
     * Check if the current user has a specific permission for a property (type-safe version).
     *
     * @param propertyId the property ID
     * @param requiredPermission the required permission
     * @return true if user has permission, false otherwise
     */
    public boolean hasPermission(Long propertyId, PropertyPermission requiredPermission) {
        Long userId = userContextService.getCurrentUserId();

        Property property = propertyService.getPropertyById(propertyId);

        if (property.getOwnerId().equals(userId)) {
            return true;
        }

        List<PropertyRoleAssignment> assignments =
                roleAssignmentRepository.findByPropertyIdAndUserId(propertyId, userId);

        if (assignments.isEmpty()) {
            return false;
        }

        // Check if any of the user's roles grant the required permission
        for (PropertyRoleAssignment assignment : assignments) {
            Set<PropertyPermission> rolePermissions = propertyRolePermissionMap.get(assignment.getRole());

            if (rolePermissions != null && rolePermissions.contains(requiredPermission)) {
                return true;
            }
        }

        return false;
    }
}
