package dev.ganeshpalankar.rentals_backend.organisation.repository;

import dev.ganeshpalankar.rentals_backend.organisation.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    @Query("SELECT o FROM Organisation o WHERE o.id IN " +
           "(SELECT ra.organisationId FROM OrganisationRoleAssignment ra WHERE ra.userId = :userId)")
    List<Organisation> findAllForUser(@Param("userId") Long userId);
}