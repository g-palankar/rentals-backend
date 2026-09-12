package dev.ganeshpalankar.rentals_backend.organisation.config;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationPermission;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
public class OrganisationPermissionConfig {

    @Bean
    public Map<OrganisationRole, Set<OrganisationPermission>> organisationRolePermissionMap() {
        return Map.of(
            OrganisationRole.ADMIN, Set.of(
                OrganisationPermission.ORG_READ,
                OrganisationPermission.ORG_EDIT,
                OrganisationPermission.ORG_DELETE,
                OrganisationPermission.ORG_MANAGE_ROLES
            ),
            OrganisationRole.MEMBER, Set.of(
                OrganisationPermission.ORG_READ
            )
        );
    }
}