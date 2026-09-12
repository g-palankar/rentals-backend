package dev.ganeshpalankar.rentals_backend.organisation.dto;

import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationResponse {
    private Long id;
    private String name;
    private OrganisationType type;
    private Long createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}