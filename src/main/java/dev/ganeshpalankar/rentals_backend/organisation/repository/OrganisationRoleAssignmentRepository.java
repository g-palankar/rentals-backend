package dev.ganeshpalankar.rentals_backend.organisation.repository;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationRoleAssignmentRepository extends JpaRepository<OrganisationRoleAssignment, Long> {

    Optional<OrganisationRoleAssignment> findByOrganisationIdAndUserId(Long organisationId, Long userId);

    List<OrganisationRoleAssignment> findByOrganisationId(Long organisationId);

    long countByOrganisationIdAndRole(Long organisationId, OrganisationRole role);

    void deleteByOrganisationIdAndUserId(Long organisationId, Long userId);

    void deleteByOrganisationId(Long organisationId);
}