package dev.ganeshpalankar.rentals_backend.property.config;

import dev.ganeshpalankar.rentals_backend.property.model.PropertyPermission;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * Configuration class that defines the mapping from roles to permissions.
 * <p>
 * This is the central place where you define what each role can do.
 * When you add new roles or change permissions, update this configuration.
 */
@Configuration
public class PropertyPermissionConfig {

    /**
     * Defines the mapping from PropertyRole to Set of PropertyPermissions.
     * <p>
     * ADMIN role: Full control - all permissions
     * VIEWER role: Read-only - only PROPERTY_READ permission
     *
     * @return Unmodifiable map of role to permission mappings
     */
    @Bean
    public Map<PropertyRole, Set<PropertyPermission>> propertyRolePermissionMap() {
        return Map.of(
            PropertyRole.ADMIN, Set.of(
                    PropertyPermission.PROPERTY_READ,
                    PropertyPermission.PROPERTY_UPDATE,
                    PropertyPermission.PROPERTY_DELETE,
                    PropertyPermission.PROPERTY_MANAGE_ROLES
            ),

            PropertyRole.VIEWER, Set.of(
                    PropertyPermission.PROPERTY_READ
            )
        );
    }
}
