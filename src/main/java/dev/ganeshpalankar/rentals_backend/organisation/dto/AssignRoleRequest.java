package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.common.validation.ValidEnum;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Action is required")
    @ValidEnum(enumClass = OrganisationRoleAction.class, message = "Invalid action. Allowed values: ASSIGN, REMOVE")
    private String action;

    // Required when action is ASSIGN, ignored when action is REMOVE
    @ValidEnum(enumClass = OrganisationRole.class, message = "Invalid role. Allowed values: ADMIN, MEMBER")
    private String role;
}