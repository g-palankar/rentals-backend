package dev.ganeshpalankar.rentals_backend.users.service;

import dev.ganeshpalankar.rentals_backend.users.exception.UserAlreadyExistsException;
import dev.ganeshpalankar.rentals_backend.users.model.User;
import dev.ganeshpalankar.rentals_backend.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User signup(String externalId) {
        if (externalId == null || externalId.trim().isEmpty()) {
            throw new RuntimeException("External ID cannot be null or empty");
        }

        if (userRepository.existsByExternalId(externalId)) {
            throw new UserAlreadyExistsException(externalId);
        }

        User user = new User();
        user.setExternalId(externalId);

        return userRepository.save(user);
    }

    @Override
    public User findByExternalId(String externalId) {
        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("User not found with external ID: " + externalId));
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return userRepository.existsByExternalId(externalId);
    }
}