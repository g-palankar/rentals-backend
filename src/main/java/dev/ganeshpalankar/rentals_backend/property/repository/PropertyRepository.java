package dev.ganeshpalankar.rentals_backend.property.repository;

import dev.ganeshpalankar.rentals_backend.property.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Property entity.
 * Provides CRUD operations and custom query methods for property management.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    /**
     * Find all properties owned by a specific user.
     *
     * @param ownerId the ID of the property owner
     * @return list of properties owned by the user
     */
    List<Property> findByOwnerId(Long ownerId);

    /**
     * Count properties owned by a specific user.
     *
     * @param ownerId the ID of the property owner
     * @return count of properties owned by the user
     */
    long countByOwnerId(Long ownerId);
}
