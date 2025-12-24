package dev.ganeshpalankar.rentals_backend.property.model;

import dev.ganeshpalankar.rentals_backend.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity representing a rental property.
 * A property is owned by a user and can contain multiple rental spaces.
 */
@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID of the user who owns this property.
     * Used in business logic and service layer.
     */
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    /**
     * JPA relationship to User entity for database foreign key constraint.
     * This field is read-only (insertable=false, updatable=false).
     * Use ownerId field for business logic instead.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_property_owner"))
    private User owner;

    @Column(name = "property_name", nullable = false, length = 200)
    private String propertyName;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "property_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * ID of the user who created this property record.
     * May differ from ownerId if an admin creates properties on behalf of owners.
     */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}