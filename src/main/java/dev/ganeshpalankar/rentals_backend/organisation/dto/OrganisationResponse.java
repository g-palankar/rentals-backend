package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationType;
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
@Schema(description = "Organisation details")
public class OrganisationResponse {
    @Schema(description = "Unique identifier of the organisation", example = "1")
    private Long id;

    @Schema(description = "Display name of the organisation", example = "Sunset Properties")
    private String name;

    @Schema(description = "Organisation type", example = "PERSONAL")
    private OrganisationType type;

    @Schema(description = "User ID of the organisation creator", example = "42")
    private Long createdBy;

    @Schema(description = "UTC timestamp when the organisation was created", example = "2024-06-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "UTC timestamp when the organisation was last updated", example = "2024-06-15T09:30:00Z")
    private Instant updatedAt;
}