package dev.ganeshpalankar.rentals_backend.users.repository;

import dev.ganeshpalankar.rentals_backend.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByExternalId(String externalId);
    boolean existsByExternalId(String externalId);
}

