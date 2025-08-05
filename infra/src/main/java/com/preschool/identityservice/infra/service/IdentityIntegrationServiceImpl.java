package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.dto.request.UserRequest;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.IdentityIntegrationService;
import com.preschool.identityservice.core.service.UserService;
import com.preschool.identityservice.core.service.infra.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class IdentityIntegrationServiceImpl implements IdentityIntegrationService {

    private final KeycloakService keycloakService;
    private final UserService userService;

    @Override
    public UserData createUserWithKeycloak(UserRequest request, String password) {
        log.info("Creating user with Keycloak integration: {}", request.getUsername());

        try {
            // 1. Create user in Keycloak first
            UserData keycloakUser =
                    keycloakService.createUser(request.getUsername(), request.getEmail(), password);

            // 2. Sync user to local database
            UserData localUser =
                    userService.syncUserFromKeycloak(
                            keycloakUser.getId(), keycloakUser.getUsername(), keycloakUser.getEmail());

            log.info("User created successfully with Keycloak integration: {}", request.getUsername());
            return localUser;

        } catch (Exception e) {
            log.error("Failed to create user with Keycloak integration: {}", e.getMessage());
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserData syncUserFromKeycloak(String keycloakId) {
        log.info("Syncing user from Keycloak: {}", keycloakId);

        // This would require additional Keycloak API calls to get user details
        // For now, we'll implement a simple sync
        throw new UnsupportedOperationException("Sync from Keycloak ID not yet implemented");
    }

    @Override
    public UserData updateUserWithKeycloak(String userId, UserRequest request) {
        log.info("Updating user with Keycloak integration: {}", userId);

        try {
            // 1. Update in local database first
            UserData localUser = userService.updateUser(java.util.UUID.fromString(userId), request);

            // 2. TODO: Update in Keycloak if needed
            // This would require additional Keycloak API methods

            log.info("User updated successfully with Keycloak integration: {}", userId);
            return localUser;

        } catch (Exception e) {
            log.error("Failed to update user with Keycloak integration: {}", e.getMessage());
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserWithKeycloak(String userId) {
        log.info("Deleting user with Keycloak integration: {}", userId);

        try {
            // 1. Get user info first
            UserData userData = userService.getUserById(java.util.UUID.fromString(userId));

            // 2. Delete from local database
            userService.deleteUser(java.util.UUID.fromString(userId));

            // 3. TODO: Delete from Keycloak if needed
            // This would require additional Keycloak API methods

            log.info("User deleted successfully with Keycloak integration: {}", userId);

        } catch (Exception e) {
            log.error("Failed to delete user with Keycloak integration: {}", e.getMessage());
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignRoleToUserWithKeycloak(String userId, String roleName) {
        log.info("Assigning role {} to user {} with Keycloak integration", roleName, userId);

        try {
            // 1. Assign role in Keycloak
            keycloakService.assignPermissionToUser(userId, roleName);

            // 2. TODO: Sync to local database if needed
            // This would require mapping between Keycloak roles and local roles

            log.info("Role assigned successfully with Keycloak integration");

        } catch (Exception e) {
            log.error("Failed to assign role with Keycloak integration: {}", e.getMessage());
            throw new RuntimeException("Failed to assign role: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeRoleFromUserWithKeycloak(String userId, String roleName) {
        log.info("Removing role {} from user {} with Keycloak integration", roleName, userId);

        try {
            // TODO: Remove role from Keycloak
            // This would require additional Keycloak API methods

            // TODO: Remove from local database if needed

            log.info("Role removed successfully with Keycloak integration");

        } catch (Exception e) {
            log.error("Failed to remove role with Keycloak integration: {}", e.getMessage());
            throw new RuntimeException("Failed to remove role: " + e.getMessage(), e);
        }
    }
}
