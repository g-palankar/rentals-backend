package dev.ganeshpalankar.rentals_backend.organisation.mapper;

import dev.ganeshpalankar.rentals_backend.organisation.dto.MemberResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.OrganisationResponse;
import dev.ganeshpalankar.rentals_backend.organisation.model.Organisation;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAssignment;
import org.springframework.stereotype.Component;

@Component
public class OrganisationMapper {

    public OrganisationResponse toResponse(Organisation organisation) {
        OrganisationResponse response = new OrganisationResponse();
        response.setId(organisation.getId());
        response.setName(organisation.getName());
        response.setType(organisation.getType());
        response.setCreatedBy(organisation.getCreatedBy());
        response.setCreatedAt(organisation.getCreatedAt());
        response.setUpdatedAt(organisation.getUpdatedAt());
        return response;
    }

    public MemberResponse toMemberResponse(OrganisationRoleAssignment assignment) {
        MemberResponse response = new MemberResponse();
        response.setUserId(assignment.getUserId());
        response.setRole(assignment.getRole());
        response.setGrantedAt(assignment.getGrantedAt());
        return response;
    }
}