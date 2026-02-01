package dev.ganeshpalankar.rentals_backend.property.model;

import dev.ganeshpalankar.rentals_backend.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity representing a role assignment for a user on a specific property.
 *
 * Key features:
 * - Users can have MULTIPLE roles for the same property
 * - Unique constraint prevents duplicate (property + user + role) combinations
 * - Property owners do NOT need entries in this table (implicit permissions)
 */
@Entity
@Table(
    name = "property_role_assignments",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_property_user_role",
        columnNames = {"property_id", "user_id", "role"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The property this role assignment applies to.
     */
    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    /**
     * JPA relationship to Property entity for database foreign key constraint.
     * Read-only field.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_property_role_property"))
    private Property property;

    /**
     * The user being granted this role.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * JPA relationship to User entity for database foreign key constraint.
     * Read-only field.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_property_role_user"))
    private User user;

    /**
     * The role assigned to this user for this property.
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyRole role;

    @Column(name = "granted_at", nullable = false)
    private Instant grantedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        grantedAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
