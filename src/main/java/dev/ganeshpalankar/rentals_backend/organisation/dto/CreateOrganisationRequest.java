package dev.ganeshpalankar.rentals_backend.organisation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new organisation")
public class CreateOrganisationRequest {

    @NotBlank(message = "Organisation name is required")
    @Schema(description = "Display name of the organisation", example = "Sunset Properties")
    private String name;
}