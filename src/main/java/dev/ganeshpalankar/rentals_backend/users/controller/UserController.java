package dev.ganeshpalankar.rentals_backend.users.controller;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ResponseBuilder;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(Authentication authentication) {
        String externalId = extractExternalIdFromJwt(authentication);
        User user = userService.signup(externalId);
        return ResponseBuilder.<User>create()
                .status(HttpStatus.CREATED)
                .message("User created successfully")
                .data(user)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findByExternalId(id.toString());
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.OK)
                    .data(user)
                    .build();
        } catch (RuntimeException e) {
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found")
                    .build();
        }
    }

    private String extractExternalIdFromJwt(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String sub = jwt.getClaimAsString("sub");
            if (sub == null || sub.trim().isEmpty()) {
                throw new RuntimeException("JWT token missing 'sub' claim");
            }
            return sub;
        }
        throw new RuntimeException("Authentication principal is not a JWT token");
    }
}

