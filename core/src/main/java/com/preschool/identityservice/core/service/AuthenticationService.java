package com.preschool.identityservice.core.service;

import com.preschool.identityservice.core.data.AccessTokenData;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.OperationData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.ExternalIdentityProviderService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service for handling authentication and authorization operations */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final ExternalIdentityProviderService externalIdentityProviderService;

    /** Retrieves access token for a user */
    public Optional<AccessTokenData> getAccessToken(String username, String password) {
        log.info("Requesting access token for username: {}", username);
        return externalIdentityProviderService.getAccessToken(username, password);
    }

    /** Creates a new user */
    public UserData createUser(String username, String email, String password) {
        log.info("Creating user: {}", username);
        return externalIdentityProviderService.createUser(username, email, password);
    }

    /** Assigns a role to a user */
    public OperationData assignPermissionToUser(String userId, String roleName) {
        log.info("Assigning role {} to user {}", roleName, userId);
        return externalIdentityProviderService.assignPermissionToUser(userId, roleName);
    }

    /** Adds a user to a group */
    public OperationData addUserToGroup(String userId, String groupId) {
        log.info("Adding user {} to group {}", userId, groupId);
        return externalIdentityProviderService.addUserToGroup(userId, groupId);
    }

    /** Creates a new group */
    public GroupData createGroup(String groupName) {
        log.info("Creating group: {}", groupName);
        return externalIdentityProviderService.createGroup(groupName);
    }

    /** Assigns a role to a group */
    public OperationData assignPermissionToGroup(String groupId, String roleName) {
        log.info("Assigning role {} to group {}", roleName, groupId);
        return externalIdentityProviderService.assignPermissionToGroup(groupId, roleName);
    }

    /** Retrieves permissions for a user */
    public List<RoleData> getUserPermissions(String userId) {
        log.info("Retrieving permissions for user {}", userId);
        return externalIdentityProviderService.getUserPermissions(userId);
    }

    /** Retrieves users with a specific role */
    public List<UserData> getUsersByPermission(String roleName) {
        log.info("Retrieving users with role {}", roleName);
        return externalIdentityProviderService.getUsersByPermission(roleName);
    }

    /** Removes a user from a group */
    public OperationData removeUserFromGroup(String userId, String groupId) {
        log.info("Removing user {} from group {}", userId, groupId);
        return externalIdentityProviderService.removeUserFromGroup(userId, groupId);
    }
}
