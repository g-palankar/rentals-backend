package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.common.validation.ValidEnum;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for inviting a new member to an organisation")
public class InviteMemberRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "A valid email address is required")
    @Schema(description = "Email address of the user to invite", example = "jane@example.com")
    private String email;

    @NotNull(message = "Role is required")
    @ValidEnum(enumClass = OrganisationRole.class, message = "Invalid role. Allowed values: ADMIN, MEMBER")
    @Schema(description = "Role to assign to the invited member. Allowed values: ADMIN, MEMBER", example = "MEMBER")
    private String role;
}
