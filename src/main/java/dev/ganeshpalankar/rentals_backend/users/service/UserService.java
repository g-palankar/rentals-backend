package dev.ganeshpalankar.rentals_backend.users.service;

import dev.ganeshpalankar.rentals_backend.users.model.User;

public interface UserService {
    User signup(String externalId);
    User findByExternalId(String externalId);
    boolean existsByExternalId(String externalId);
}