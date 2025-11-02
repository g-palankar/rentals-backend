package dev.ganeshpalankar.rentals_backend.users.service;

import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredException;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Service for accessing information about the currently authenticated user.
 * Extracts user information from JWT token and resolves internal user ID.
 */
@Service
@RequiredArgsConstructor
public class UserContextService {

    private final UserRepository userRepository;

    /**
     * Get the internal user ID of the currently authenticated user.
     *
     * @return the internal user ID
     * @throws RuntimeException if user is not authenticated or not found
     */
    public Long getCurrentUserId() {
        String externalId = getCurrentUserExternalId();
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotRegisteredException(externalId));
        return user.getId();
    }

    /**
     * Get the external ID (from JWT 'sub' claim) of the currently authenticated user.
     *
     * @return the external ID from the JWT token
     * @throws RuntimeException if user is not authenticated or JWT is invalid
     */
    public String getCurrentUserExternalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        throw new RuntimeException("Invalid authentication type");
    }

    /**
     * Get the full User object of the currently authenticated user.
     *
     * @return the User entity
     * @throws RuntimeException if user is not authenticated or not found
     */
    public User getCurrentUser() {
        String externalId = getCurrentUserExternalId();
        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new UserNotRegisteredException(externalId));
    }
}
