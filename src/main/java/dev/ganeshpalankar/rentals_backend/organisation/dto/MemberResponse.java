package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Organisation member details")
public class MemberResponse {
    @Schema(description = "User ID of the member", example = "7")
    private Long userId;

    @Schema(description = "Role assigned to the member within the organisation", example = "MEMBER")
    private OrganisationRole role;

    @Schema(description = "UTC timestamp when the role was granted", example = "2024-06-01T12:00:00Z")
    private Instant grantedAt;
}
