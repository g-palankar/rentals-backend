package dev.ganeshpalankar.rentals_backend.property.model;

/**
 * Enum representing roles that can be assigned to users for a specific property.
 * <p>
 * Roles define a collection of permissions. The mapping of roles to permissions
 * is configured in PropertyPermissionConfig.
 * <p>
 * Users can have multiple roles for the same property. The effective permissions
 * are the union of all permissions granted by their roles.
 */
public enum PropertyRole {

    /**
     * Administrator role - full control over the property.
     * Typically, grants all permissions including role management.
     * <p>
     * Note: Property owners implicitly have all permissions without needing
     * an explicit ADMIN role assignment.
     */
    ADMIN,

    /**
     * Viewer role - read-only access to the property.
     */
    VIEWER

    // Future roles can be added here:
    // MANAGER,    // Can manage tenants and leases
    // ACCOUNTANT, // Can view financial data
    // MAINTENANCE // Can view maintenance requests
}
