package dev.ganeshpalankar.rentals_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ganeshpalankar.rentals_backend.common.exception.ErrorType;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorDetail;
import dev.ganeshpalankar.rentals_backend.common.response.ErrorResponse;
import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredException;
import dev.ganeshpalankar.rentals_backend.users.exception.UserNotRegisteredExceptionHandler;
import dev.ganeshpalankar.rentals_backend.users.service.UserContextService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter that verifies authenticated users are registered in the database.
 * Runs after JWT authentication but before authorization checks.
 * Skips public endpoints that don't require authentication.
 */
@Component
@RequiredArgsConstructor
public class RegisteredUserFilter extends OncePerRequestFilter {

    private static final List<String> SKIP_REGISTRATION_CHECK_PATTERNS = List.of(
            "/public/**",
            "/users/signup"
    );

    private final UserContextService userContextService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip registration check for specific endpoints
        if (shouldSkipRegistrationCheck(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            try {
                // Verify user is registered in database
                userContextService.getCurrentUserId();
            } catch (UserNotRegisteredException ex) {
                // User is authenticated but not registered - return 403 Forbidden
                sendErrorResponse(response, request, ex);
                return;
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }


    private boolean shouldSkipRegistrationCheck(String uri) {
        return SKIP_REGISTRATION_CHECK_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpServletRequest request,
                                   UserNotRegisteredException ex) throws IOException {

        ErrorResponse errorResponse = new UserNotRegisteredExceptionHandler()
                .handle(ex,request);

        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
