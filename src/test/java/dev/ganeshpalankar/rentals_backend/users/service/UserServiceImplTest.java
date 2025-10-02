package dev.ganeshpalankar.rentals_backend.users.service;

import dev.ganeshpalankar.rentals_backend.users.exception.UserAlreadyExistsException;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private String validExternalId;
    private User savedUser;

    @BeforeEach
    void setUp() {
        validExternalId = "auth0|123456789";

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setExternalId(validExternalId);
    }

    // ========== signup() tests ==========

    @Test
    @DisplayName("signup() - Should successfully create user with valid externalId")
    void signup_WithValidExternalId_ShouldCreateUser() {
        // Given
        when(userRepository.existsByExternalId(validExternalId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.signup(validExternalId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getExternalId()).isEqualTo(validExternalId);

        verify(userRepository).existsByExternalId(validExternalId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("signup() - Should throw UserAlreadyExistsException when user exists")
    void signup_WhenUserExists_ShouldThrowUserAlreadyExistsException() {
        // Given
        when(userRepository.existsByExternalId(validExternalId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signup(validExternalId))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("externalId", validExternalId);

        verify(userRepository).existsByExternalId(validExternalId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("signup() - Should throw RuntimeException when externalId is null")
    void signup_WithNullExternalId_ShouldThrowRuntimeException() {
        // When & Then
        assertThatThrownBy(() -> userService.signup(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("External ID cannot be null or empty");

        verify(userRepository, never()).existsByExternalId(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("signup() - Should throw RuntimeException when externalId is empty")
    void signup_WithEmptyExternalId_ShouldThrowRuntimeException() {
        // When & Then
        assertThatThrownBy(() -> userService.signup(""))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("External ID cannot be null or empty");

        verify(userRepository, never()).existsByExternalId(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("signup() - Should throw RuntimeException when externalId is blank")
    void signup_WithBlankExternalId_ShouldThrowRuntimeException() {
        // When & Then
        assertThatThrownBy(() -> userService.signup("   "))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("External ID cannot be null or empty");

        verify(userRepository, never()).existsByExternalId(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("signup() - Should save user with externalId set correctly")
    void signup_ShouldSaveUserWithCorrectExternalId() {
        // Given
        when(userRepository.existsByExternalId(validExternalId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // When
        User result = userService.signup(validExternalId);

        // Then
        verify(userRepository).save(argThat(user ->
            user.getExternalId().equals(validExternalId)
        ));
    }

    // ========== findByExternalId() tests ==========

    @Test
    @DisplayName("findByExternalId() - Should return user when found")
    void findByExternalId_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByExternalId(validExternalId)).thenReturn(Optional.of(savedUser));

        // When
        User result = userService.findByExternalId(validExternalId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getExternalId()).isEqualTo(validExternalId);

        verify(userRepository).findByExternalId(validExternalId);
    }

    @Test
    @DisplayName("findByExternalId() - Should throw RuntimeException when user not found")
    void findByExternalId_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findByExternalId(validExternalId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findByExternalId(validExternalId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with external ID: " + validExternalId);

        verify(userRepository).findByExternalId(validExternalId);
    }

    // ========== existsByExternalId() tests ==========

    @Test
    @DisplayName("existsByExternalId() - Should return true when user exists")
    void existsByExternalId_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByExternalId(validExternalId)).thenReturn(true);

        // When
        boolean result = userService.existsByExternalId(validExternalId);

        // Then
        assertThat(result).isTrue();
        verify(userRepository).existsByExternalId(validExternalId);
    }

    @Test
    @DisplayName("existsByExternalId() - Should return false when user does not exist")
    void existsByExternalId_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByExternalId(validExternalId)).thenReturn(false);

        // When
        boolean result = userService.existsByExternalId(validExternalId);

        // Then
        assertThat(result).isFalse();
        verify(userRepository).existsByExternalId(validExternalId);
    }
}