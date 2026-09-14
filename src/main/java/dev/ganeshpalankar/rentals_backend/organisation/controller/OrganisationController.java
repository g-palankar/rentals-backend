package dev.ganeshpalankar.rentals_backend.organisation.controller;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Organisations", description = "Manage organisations and their members")
public class OrganisationController {

    private final OrganisationService organisationService;
    private final UserContextService userContextService;
    private final OrganisationMapper organisationMapper;

    @Operation(summary = "Create organisation", description = "Creates a new organisation owned by the authenticated user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Organisation created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"name": "Sunset Properties"}
                    """)))
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

    @Operation(summary = "List organisations", description = "Returns all organisations the authenticated user belongs to.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisations retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Get organisation by ID", description = "Returns a single organisation. Requires ORG_READ permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisation retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Invite member", description = "Adds a user to the organisation by email. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Member added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or user not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"email": "jane@example.com", "role": "MEMBER"}
                    """)))
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

    @Operation(summary = "List members", description = "Returns all members of an organisation. Requires ORG_READ permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Members retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Update member role", description = "Changes a member's role within the organisation. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member role updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or member not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"role": "ADMIN"}
                    """)))
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

    @Operation(summary = "Remove member", description = "Removes a member from the organisation. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Member removed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or member not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_MANAGE_ROLES')")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId) {
        organisationService.removeMember(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update organisation", description = "Updates organisation details. Requires ORG_EDIT permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisation updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"name": "Sunrise Properties"}
                    """)))
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

    @Operation(summary = "Delete organisation", description = "Permanently deletes an organisation. Requires ORG_DELETE permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Organisation deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@organisationPermissionService.hasPermission(#id, 'ORG_DELETE')")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }

}
