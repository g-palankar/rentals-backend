package dev.ganeshpalankar.rentals_backend.organisation.model;

import dev.ganeshpalankar.rentals_backend.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a user's role within an organisation.
 *
 * Each user holds at most one role per organisation (unique on user_id + organisation_id).
 * Organisation creators do NOT need an entry here — they get implicit all-permissions.
 */
@Entity
@Table(
    name = "organisation_role_assignments",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_organisation_user",
        columnNames = {"organisation_id", "user_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organisation_id", nullable = false)
    private Long organisationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_org_role_organisation"))
    private Organisation organisation;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_org_role_user"))
    private User user;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganisationRole role;

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