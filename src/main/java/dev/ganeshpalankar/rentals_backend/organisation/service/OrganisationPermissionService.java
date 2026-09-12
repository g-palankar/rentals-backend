package dev.ganeshpalankar.rentals_backend.organisation.service;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationPermission;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAssignment;
import dev.ganeshpalankar.rentals_backend.organisation.repository.OrganisationRoleAssignmentRepository;
import dev.ganeshpalankar.rentals_backend.users.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganisationPermissionService {

    private final OrganisationService organisationService;
    private final OrganisationRoleAssignmentRepository roleAssignmentRepository;
    private final UserContextService userContextService;
    private final Map<OrganisationRole, Set<OrganisationPermission>> organisationRolePermissionMap;

    public boolean hasPermission(Long organisationId, String permissionName) {
        try {
            OrganisationPermission permission = OrganisationPermission.valueOf(permissionName);
            return hasPermission(organisationId, permission);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean hasPermission(Long organisationId, OrganisationPermission requiredPermission) {
        Long userId = userContextService.getCurrentUserId();

        organisationService.getOrganisationById(organisationId);

        Optional<OrganisationRoleAssignment> assignment =
                roleAssignmentRepository.findByOrganisationIdAndUserId(organisationId, userId);

        if (assignment.isEmpty()) {
            return false;
        }

        Set<OrganisationPermission> rolePermissions =
                organisationRolePermissionMap.get(assignment.get().getRole());

        return rolePermissions != null && rolePermissions.contains(requiredPermission);
    }
}
