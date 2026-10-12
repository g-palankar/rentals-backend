package dev.ganeshpalankar.rentals_backend.organisation.api;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.CreateOrganisationRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.InviteMemberRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.MemberResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.OrganisationResponse;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateMemberRoleRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateOrganisationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Organisations", description = "Manage organisations and their members")
public interface OrganisationApi {

    @Operation(summary = "Create organisation", description = "Creates a new organisation owned by the authenticated user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Organisation created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Validation failed", value = """
                                    {
                                      "status": 400,
                                      "message": "Validation failed",
                                      "error": {"code": "VALIDATION_FAILED", "type": "VALIDATION_ERROR", "details": "1 validation error(s) occurred"},
                                      "path": "/api/organisations",
                                      "method": "POST",
                                      "fieldErrors": [{"field": "name", "messages": ["Organisation name is required"], "rejectedValue": \"\"}],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Malformed body", value = """
                                    {
                                      "status": 400,
                                      "message": "Invalid request body",
                                      "error": {"code": "INVALID_REQUEST_BODY", "type": "VALIDATION_ERROR", "details": "JSON parse error"},
                                      "path": "/api/organisations",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "An organisation with this name already exists for the creator",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Name already exists", value = """
                                    {
                                      "status": 409,
                                      "message": "Organisation name already exists",
                                      "error": {"code": "ORGANISATION_NAME_ALREADY_EXISTS", "type": "BUSINESS_LOGIC_ERROR", "details": "An organisation named 'Sunset Properties' already exists"},
                                      "path": "/api/organisations",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"name": "Sunset Properties"}
                    """)))
    ResponseEntity<ApiResponse<OrganisationResponse>> createOrganisation(CreateOrganisationRequest request);

    @Operation(summary = "List organisations", description = "Returns all organisations the authenticated user belongs to.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisations retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    ResponseEntity<ApiResponse<List<OrganisationResponse>>> getOrganisations();

    @Operation(summary = "Get organisation by ID", description = "Returns a single organisation. Requires ORG_READ permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisation retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    ResponseEntity<ApiResponse<OrganisationResponse>> getOrganisation(Long id);

    @Operation(summary = "Invite member", description = "Adds a user to the organisation by email. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Member added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Validation failed", value = """
                                    {
                                      "status": 400,
                                      "message": "Validation failed",
                                      "error": {"code": "VALIDATION_FAILED", "type": "VALIDATION_ERROR", "details": "1 validation error(s) occurred"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [{"field": "email", "messages": ["Email is required"], "rejectedValue": null}],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Malformed body", value = """
                                    {
                                      "status": 400,
                                      "message": "Invalid request body",
                                      "error": {"code": "INVALID_REQUEST_BODY", "type": "VALIDATION_ERROR", "details": "JSON parse error"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or user not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "User not found", value = """
                                    {
                                      "status": 404,
                                      "message": "User not found",
                                      "error": {"code": "USER_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "No registered user found with email 'jane@example.com'"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User is already a member",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Already a member", value = """
                                    {
                                      "status": 409,
                                      "message": "User is already a member of this organisation",
                                      "error": {"code": "MEMBER_ALREADY_EXISTS", "type": "BUSINESS_LOGIC_ERROR", "details": "User '7' is already a member of organisation '42'"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42/members",
                                      "method": "POST",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"email": "jane@example.com", "role": "MEMBER"}
                    """)))
    ResponseEntity<ApiResponse<MemberResponse>> inviteMember(Long id, InviteMemberRequest request);

    @Operation(summary = "List members", description = "Returns all members of an organisation. Requires ORG_READ permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Members retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42/members",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42/members",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42/members",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42/members",
                                      "method": "GET",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    ResponseEntity<ApiResponse<List<MemberResponse>>> getMembers(Long id);

    @Operation(summary = "Update member role", description = "Changes a member's role within the organisation. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member role updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Validation failed", value = """
                                    {
                                      "status": 400,
                                      "message": "Validation failed",
                                      "error": {"code": "VALIDATION_FAILED", "type": "VALIDATION_ERROR", "details": "1 validation error(s) occurred"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [{"field": "role", "messages": ["Role is required"], "rejectedValue": null}],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Malformed body", value = """
                                    {
                                      "status": 400,
                                      "message": "Invalid request body",
                                      "error": {"code": "INVALID_REQUEST_BODY", "type": "VALIDATION_ERROR", "details": "JSON parse error"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or member not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Would demote the last admin",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Last admin", value = """
                                    {
                                      "status": 409,
                                      "message": "Cannot remove or demote the last admin of an organisation",
                                      "error": {"code": "LAST_ADMIN", "type": "BUSINESS_LOGIC_ERROR", "details": "Organisation '42' must retain at least one admin"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"role": "ADMIN"}
                    """)))
    ResponseEntity<ApiResponse<MemberResponse>> updateMemberRole(Long id, Long userId, UpdateMemberRoleRequest request);

    @Operation(summary = "Remove member", description = "Removes a member from the organisation. Requires ORG_MANAGE_ROLES permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Member removed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation or member not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Would remove the last admin",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Last admin", value = """
                                    {
                                      "status": 409,
                                      "message": "Cannot remove or demote the last admin of an organisation",
                                      "error": {"code": "LAST_ADMIN", "type": "BUSINESS_LOGIC_ERROR", "details": "Organisation '42' must retain at least one admin"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42/members/7",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    ResponseEntity<Void> removeMember(Long id, Long userId);

    @Operation(summary = "Update organisation", description = "Updates organisation details. Requires ORG_EDIT permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organisation updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Validation failed", value = """
                                    {
                                      "status": 400,
                                      "message": "Validation failed",
                                      "error": {"code": "VALIDATION_FAILED", "type": "VALIDATION_ERROR", "details": "1 validation error(s) occurred"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [{"field": "name", "messages": ["Organisation name is required"], "rejectedValue": \"\"}],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Malformed body", value = """
                                    {
                                      "status": 400,
                                      "message": "Invalid request body",
                                      "error": {"code": "INVALID_REQUEST_BODY", "type": "VALIDATION_ERROR", "details": "JSON parse error"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Personal organisation", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "Personal organisations cannot be renamed"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "An organisation with this name already exists for the creator",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Name already exists", value = """
                                    {
                                      "status": 409,
                                      "message": "Organisation name already exists",
                                      "error": {"code": "ORGANISATION_NAME_ALREADY_EXISTS", "type": "BUSINESS_LOGIC_ERROR", "details": "An organisation named 'Sunrise Properties' already exists"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42",
                                      "method": "PATCH",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = """
                    {"name": "Sunrise Properties"}
                    """)))
    ResponseEntity<ApiResponse<OrganisationResponse>> updateOrganisation(Long id, UpdateOrganisationRequest request);

    @Operation(summary = "Delete organisation", description = "Permanently deletes an organisation. Requires ORG_DELETE permission.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Organisation deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing or invalid bearer token. Returned by Spring Security with an empty body and a WWW-Authenticate: Bearer header", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User has not completed signup, or lacks the required permission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "User not registered", value = """
                                    {
                                      "status": 403,
                                      "message": "User not registered",
                                      "error": {"code": "USER_NOT_REGISTERED", "type": "AUTHORIZATION_ERROR", "details": "User with external ID 'auth0|abc123' is not registered. Please complete signup."},
                                      "path": "/api/organisations/42",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """),
                            @ExampleObject(name = "Insufficient permissions", value = """
                                    {
                                      "status": 403,
                                      "message": "Access denied",
                                      "error": {"code": "FORBIDDEN", "type": "AUTHORIZATION_ERROR", "details": "User lacks the required organisation permission"},
                                      "path": "/api/organisations/42",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organisation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Organisation not found", value = """
                                    {
                                      "status": 404,
                                      "message": "Organisation not found",
                                      "error": {"code": "ORGANISATION_NOT_FOUND", "type": "RESOURCE_NOT_FOUND", "details": "Organisation with ID '42' does not exist"},
                                      "path": "/api/organisations/42",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Organisation still has properties",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Has properties", value = """
                                    {
                                      "status": 409,
                                      "message": "Cannot delete an organisation that has properties",
                                      "error": {"code": "ORG_HAS_PROPERTIES", "type": "BUSINESS_LOGIC_ERROR", "details": "Organisation '42' still has properties linked to it"},
                                      "path": "/api/organisations/42",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Server error", value = """
                                    {
                                      "status": 500,
                                      "message": "Internal server error",
                                      "error": {"code": "INTERNAL_ERROR", "type": "SERVER_ERROR", "details": "An unexpected error occurred"},
                                      "path": "/api/organisations/42",
                                      "method": "DELETE",
                                      "fieldErrors": [],
                                      "timestamp": "2024-06-01T12:00:00Z"
                                    }
                                    """)
                    }))
    })
    ResponseEntity<Void> deleteOrganisation(Long id);
}
