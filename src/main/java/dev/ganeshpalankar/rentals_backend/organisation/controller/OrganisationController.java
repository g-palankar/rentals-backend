package dev.ganeshpalankar.rentals_backend.organisation.controller;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ResponseBuilder;
import dev.ganeshpalankar.rentals_backend.organisation.dto.CreateOrganisationRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.InviteMemberRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.MemberResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.OrganisationResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateMemberRoleRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateOrganisationRequest;
import dev.ganeshpalankar.rentals_backend.organisation.mapper.OrganisationMapper;
import dev.ganeshpalankar.rentals_backend.organisation.model.Organisation;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAssignment;
import dev.ganeshpalankar.rentals_backend.organisation.service.OrganisationService;
import dev.ganeshpalankar.rentals_backend.users.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;
    private final UserContextService userContextService;
    private final OrganisationMapper organisationMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganisationResponse>> createOrganisation(
            @Valid @RequestBody CreateOrganisationRequest request) {
        Long currentUserId = userContextService.getCurrentUserId();
        Organisation org = organisationService.createOrganisation(request, currentUserId);
        return ResponseBuilder.<OrganisationResponse>create()
                .status(HttpStatus.CREATED)
                .message("Organisation created successfully")
                .data(organisationMapper.toResponse(org))
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganisationResponse>>> getOrganisations() {
        Long currentUserId = userContextService.getCurrentUserId();
        List<OrganisationResponse> responses = organisationService.getOrganisationsForUser(currentUserId)
                .stream()
                .map(organisationMapper::toResponse)
                .toList();
        return ResponseBuilder.<List<OrganisationResponse>>create()
                .status(HttpStatus.OK)
                .message("Organisations retrieved successfully")
                .data(responses)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_READ')")
    public ResponseEntity<ApiResponse<OrganisationResponse>> getOrganisation(@PathVariable Long id) {
        Organisation org = organisationService.getOrganisationById(id);
        return ResponseBuilder.<OrganisationResponse>create()
                .status(HttpStatus.OK)
                .message("Organisation retrieved successfully")
                .data(organisationMapper.toResponse(org))
                .build();
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<MemberResponse>> inviteMember(
            @PathVariable Long id,
            @Valid @RequestBody InviteMemberRequest request) {
        OrganisationRoleAssignment assignment = organisationService.inviteMember(id, request);
        return ResponseBuilder.<MemberResponse>create()
                .status(HttpStatus.CREATED)
                .message("Member added successfully")
                .data(organisationMapper.toMemberResponse(assignment))
                .build();
    }

    @GetMapping("/{id}/members")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_READ')")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getMembers(@PathVariable Long id) {
        List<MemberResponse> responses = organisationService.getMembers(id)
                .stream()
                .map(organisationMapper::toMemberResponse)
                .toList();
        return ResponseBuilder.<List<MemberResponse>>create()
                .status(HttpStatus.OK)
                .message("Members retrieved successfully")
                .data(responses)
                .build();
    }

    @PatchMapping("/{id}/members/{userId}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMemberRole(
            @PathVariable Long id,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateMemberRoleRequest request) {
        OrganisationRoleAssignment assignment = organisationService.updateMemberRole(id, userId, request);
        return ResponseBuilder.<MemberResponse>create()
                .status(HttpStatus.OK)
                .message("Member role updated successfully")
                .data(organisationMapper.toMemberResponse(assignment))
                .build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_MANAGE_ROLES')")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId) {
        organisationService.removeMember(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_EDIT')")
    public ResponseEntity<ApiResponse<OrganisationResponse>> updateOrganisation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganisationRequest request) {
        Organisation org = organisationService.updateOrganisation(id, request);
        return ResponseBuilder.<OrganisationResponse>create()
                .status(HttpStatus.OK)
                .message("Organisation updated successfully")
                .data(organisationMapper.toResponse(org))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_DELETE')")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }

}
