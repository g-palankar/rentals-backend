package dev.ganeshpalankar.rentals_backend.property.repository;

import dev.ganeshpalankar.rentals_backend.property.model.PropertyRole;
import dev.ganeshpalankar.rentals_backend.property.model.PropertyRoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing property role assignments.
 *
 * Note: findByPropertyIdAndUserId returns a List (not Optional) because
 * users can have multiple roles for the same property.
 */
@Repository
public interface PropertyRoleAssignmentRepository extends JpaRepository<PropertyRoleAssignment, Long> {

    /**
     * Find all role assignments for a specific user on a specific property.
     *
     * Returns a List because users can have multiple roles.
     * Optimized by composite index: idx_property_role_lookup (property_id, user_id)
     *
     * @param propertyId the property ID
     * @param userId the user ID
     * @return List of role assignments (may be empty, never null)
     */
    List<PropertyRoleAssignment> findByPropertyIdAndUserId(Long propertyId, Long userId);

}
