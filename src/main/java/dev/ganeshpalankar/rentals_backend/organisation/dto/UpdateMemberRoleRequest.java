package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.common.validation.ValidEnum;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role is required")
    @ValidEnum(enumClass = OrganisationRole.class, message = "Invalid role. Allowed values: ADMIN, MEMBER")
    private String role;
}
