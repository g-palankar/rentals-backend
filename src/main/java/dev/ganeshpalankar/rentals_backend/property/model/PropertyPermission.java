package dev.ganeshpalankar.rentals_backend.property.model;

/**
 * Enum representing permissions that can be granted for property access.
 *
 * These permissions define what actions a user can perform on a property.
 * Permissions are mapped to roles via PropertyPermissionConfig.
 */
public enum PropertyPermission {
    PROPERTY_READ,
    PROPERTY_UPDATE,
    PROPERTY_DELETE,
    PROPERTY_MANAGE_ROLES
}