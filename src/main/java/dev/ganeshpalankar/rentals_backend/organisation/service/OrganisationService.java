package dev.ganeshpalankar.rentals_backend.organisation.service;

import dev.ganeshpalankar.rentals_backend.common.exception.ForbiddenException;
import dev.ganeshpalankar.rentals_backend.common.exception.ResourceNotFoundException;
import dev.ganeshpalankar.rentals_backend.organisation.dto.CreateOrganisationRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.InviteMemberRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateMemberRoleRequest;
import dev.ganeshpalankar.rentals_backend.organisation.dto.UpdateOrganisationRequest;
import dev.ganeshpalankar.rentals_backend.organisation.exception.LastAdminException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.MemberAlreadyExistsException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrgHasPropertiesException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.OrganisationNotFoundException;
import dev.ganeshpalankar.rentals_backend.organisation.exception.UserNotFoundByEmailException;
import dev.ganeshpalankar.rentals_backend.organisation.model.Organisation;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRole;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationRoleAssignment;
import dev.ganeshpalankar.rentals_backend.organisation.model.OrganisationType;
import dev.ganeshpalankar.rentals_backend.organisation.repository.OrganisationRepository;
import dev.ganeshpalankar.rentals_backend.organisation.repository.OrganisationRoleAssignmentRepository;
import dev.ganeshpalankar.rentals_backend.property.repository.PropertyRepository;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final OrganisationRoleAssignmentRepository roleAssignmentRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    @Transactional
    public Organisation createOrganisation(CreateOrganisationRequest request, Long creatorId) {
        Organisation org = new Organisation();
        org.setName(request.getName());
        org.setType(OrganisationType.COMPANY);
        org.setCreatedBy(creatorId);
        org = organisationRepository.save(org);

        OrganisationRoleAssignment assignment = new OrganisationRoleAssignment();
        assignment.setOrganisationId(org.getId());
        assignment.setUserId(creatorId);
        assignment.setRole(OrganisationRole.ADMIN);
        roleAssignmentRepository.save(assignment);

        return org;
    }

    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new OrganisationNotFoundException(id));
    }

    public List<Organisation> getOrganisationsForUser(Long userId) {
        return organisationRepository.findAllForUser(userId);
    }

    @Transactional
    public OrganisationRoleAssignment inviteMember(Long orgId, InviteMemberRequest request) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Member management is not allowed for personal organisations");

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundByEmailException(request.getEmail()));

        if (roleAssignmentRepository.findByOrganisationIdAndUserId(orgId, user.getId()).isPresent()) {
            throw new MemberAlreadyExistsException(orgId, user.getId());
        }

        OrganisationRoleAssignment assignment = new OrganisationRoleAssignment();
        assignment.setOrganisationId(orgId);
        assignment.setUserId(user.getId());
        assignment.setRole(OrganisationRole.valueOf(request.getRole()));
        return roleAssignmentRepository.save(assignment);
    }

    public List<OrganisationRoleAssignment> getMembers(Long orgId) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Member management is not allowed for personal organisations");
        return roleAssignmentRepository.findByOrganisationId(orgId);
    }

    @Transactional
    public OrganisationRoleAssignment updateMemberRole(Long orgId, Long userId, UpdateMemberRoleRequest request) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Member management is not allowed for personal organisations");

        OrganisationRoleAssignment assignment = roleAssignmentRepository
                .findByOrganisationIdAndUserId(orgId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, "Member"));

        OrganisationRole newRole = OrganisationRole.valueOf(request.getRole());
        if (assignment.getRole() == OrganisationRole.ADMIN && newRole != OrganisationRole.ADMIN) {
            long adminCount = roleAssignmentRepository.countByOrganisationIdAndRole(orgId, OrganisationRole.ADMIN);
            if (adminCount <= 1) {
                throw new LastAdminException(orgId);
            }
        }

        assignment.setRole(newRole);
        return roleAssignmentRepository.save(assignment);
    }

    @Transactional
    public void removeMember(Long orgId, Long userId) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Member management is not allowed for personal organisations");

        OrganisationRoleAssignment assignment = roleAssignmentRepository
                .findByOrganisationIdAndUserId(orgId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, "Member"));

        if (assignment.getRole() == OrganisationRole.ADMIN) {
            long adminCount = roleAssignmentRepository.countByOrganisationIdAndRole(orgId, OrganisationRole.ADMIN);
            if (adminCount <= 1) {
                throw new LastAdminException(orgId);
            }
        }

        roleAssignmentRepository.delete(assignment);
    }

    @Transactional
    public Organisation updateOrganisation(Long orgId, UpdateOrganisationRequest request) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Personal organisations cannot be renamed");
        org.setName(request.getName());
        return organisationRepository.save(org);
    }

    @Transactional
    public void deleteOrganisation(Long orgId) {
        Organisation org = getOrganisationById(orgId);
        assertNotPersonal(org, "Personal organisations cannot be deleted");
        if (propertyRepository.existsByOrganisationId(orgId)) {
            throw new OrgHasPropertiesException(orgId);
        }
        roleAssignmentRepository.deleteByOrganisationId(orgId);
        organisationRepository.delete(org);
    }

    private void assertNotPersonal(Organisation org, String reason) {
        if (org.getType() == OrganisationType.PERSONAL) {
            throw new ForbiddenException(reason);
        }
    }
}
