package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.common.validation.ValidEnum;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for updating a member's role in an organisation")
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role is required")
    @ValidEnum(enumClass = OrganisationRole.class, message = "Invalid role. Allowed values: ADMIN, MEMBER")
    @Schema(description = "New role to assign to the member. Allowed values: ADMIN, MEMBER", example = "ADMIN")
    private String role;
}
