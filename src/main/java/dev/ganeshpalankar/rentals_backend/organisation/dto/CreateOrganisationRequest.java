package dev.ganeshpalankar.rentals_backend.organisation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganisationRequest {

    @NotBlank(message = "Organisation name is required")
    private String name;
}