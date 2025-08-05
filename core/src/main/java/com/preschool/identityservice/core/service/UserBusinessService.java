package com.preschool.identityservice.core.service;

import com.preschool.identityservice.common.exception.InvalidDataException;
import com.preschool.identityservice.common.param.UserParam;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.UserDataAccessService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Business logic service for User operations Contains all business rules and validation for user
 * management
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserBusinessService {

    private final UserDataAccessService userDataAccessService;

    public UserData createUser(UserParam param) {
        log.info("Creating user with username: {}", param.getUsername());

        // Business validation
        validateUserCreation(param);

        UserData result = userDataAccessService.createUser(param);
        log.info("User created successfully with ID: {}", result.getUserId());

        return result;
    }

    @Transactional(readOnly = true)
    public UserData getUserById(UUID userId) {
        log.info("Getting user by ID: {}", userId);
        return userDataAccessService.getUserById(userId);
    }

    @Transactional(readOnly = true)
    public UserData getUserByKeycloakId(String keycloakId) {
        log.info("Getting user by Keycloak ID: {}", keycloakId);
        return userDataAccessService.getUserByKeycloakId(keycloakId);
    }

    @Transactional(readOnly = true)
    public UserData getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userDataAccessService.getUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<UserData> getAllUsers() {
        log.info("Getting all users");
        return userDataAccessService.getAllUsers();
    }

    public UserData updateUser(UUID userId, UserParam param) {
        log.info("Updating user with ID: {}", userId);

        // Business validation
        validateUserUpdate(param);

        UserData result = userDataAccessService.updateUser(userId, param);
        log.info("User updated successfully with ID: {}", userId);

        return result;
    }

    public void deleteUser(UUID userId) {
        log.info("Deleting user with ID: {}", userId);

        // Business validation - check if user can be deleted
        validateUserDeletion(userId);

        userDataAccessService.deleteUser(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    public UserData syncUserFromKeycloak(String keycloakId, String username, String email) {
        log.info("Syncing user from Keycloak: {}", username);
        return userDataAccessService.syncUserFromKeycloak(keycloakId, username, email);
    }

    public void assignRoleToUser(UUID userId, UUID roleId) {
        log.info("Assigning role {} to user {}", roleId, userId);

        // Business validation
        validateRoleAssignment(userId, roleId);

        userDataAccessService.assignRoleToUser(userId, roleId);
        log.info("Role assigned successfully");
    }

    public void removeRoleFromUser(UUID userId, UUID roleId) {
        log.info("Removing role {} from user {}", roleId, userId);

        // Business validation
        validateRoleRemoval(userId, roleId);

        userDataAccessService.removeRoleFromUser(userId, roleId);
        log.info("Role removed successfully");
    }

    @Transactional(readOnly = true)
    public List<UserData> getUsersByRole(UUID roleId) {
        log.info("Getting users by role ID: {}", roleId);
        return userDataAccessService.getUsersByRole(roleId);
    }

    // Private helper methods for business logic
    private void validateUserCreation(UserParam param) {
        if (!StringUtils.hasText(param.getUsername())) {
            throw new InvalidDataException("Username cannot be empty");
        }
        if (!StringUtils.hasText(param.getEmail())) {
            throw new InvalidDataException("Email cannot be empty");
        }

        // Add more business rules as needed
        if (param.getUsername().length() < 3) {
            throw new InvalidDataException("Username must be at least 3 characters long");
        }

        // Basic email validation
        if (!param.getEmail().contains("@")) {
            throw new InvalidDataException("Invalid email format");
        }
    }

    private void validateUserUpdate(UserParam param) {
        if (!StringUtils.hasText(param.getUsername())) {
            throw new InvalidDataException("Username cannot be empty");
        }
        if (!StringUtils.hasText(param.getEmail())) {
            throw new InvalidDataException("Email cannot be empty");
        }
    }

    private void validateUserDeletion(UUID userId) {
        // Check if user can be safely deleted
        // Add business rules like checking if user has active sessions, etc.
        log.debug("Validating user deletion for ID: {}", userId);
    }

    private void validateRoleAssignment(UUID userId, UUID roleId) {
        // Add business rules for role assignment
        log.debug("Validating role assignment: user {} role {}", userId, roleId);
    }

    private void validateRoleRemoval(UUID userId, UUID roleId) {
        // Add business rules for role removal
        log.debug("Validating role removal: user {} role {}", userId, roleId);
    }
}
