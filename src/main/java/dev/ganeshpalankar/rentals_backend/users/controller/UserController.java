package dev.ganeshpalankar.rentals_backend.users.controller;

import dev.ganeshpalankar.rentals_backend.common.response.ApiResponse;
import dev.ganeshpalankar.rentals_backend.common.response.ResponseBuilder;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(Authentication authentication) {
        String externalId = extractClaimFromJwt(authentication, "sub");
        String email = extractClaimFromJwt(authentication, "email");
        User user = userService.signup(externalId, email);
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

    private String extractClaimFromJwt(Authentication authentication, String claim) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString(claim);
        }
        throw new RuntimeException("Authentication principal is not a JWT token");
    }
}
