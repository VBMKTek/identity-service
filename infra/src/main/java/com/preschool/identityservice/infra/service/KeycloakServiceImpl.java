package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.enumeration.KeycloakErrorCode;
import com.preschool.identityservice.common.exception.KeycloakException;
import com.preschool.identityservice.common.properties.KeycloakProperties;
import com.preschool.identityservice.core.data.AccessTokenData;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.OperationData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.ExternalIdentityProviderService;
import com.preschool.identityservice.infra.mapper.AccessTokenMapper;
import com.preschool.identityservice.infra.mapper.KeycloakMapper;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

/** Service for Keycloak integration */
@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements ExternalIdentityProviderService {
    private final KeycloakProperties keycloakProperties;
    private final KeycloakBuilder keycloakBuilder;
    private final Keycloak keycloak;
    private final AccessTokenMapper accessTokenMapper;
    private final KeycloakMapper keycloakMapper;

    /** Gets access token for user */
    @Override
    public Optional<AccessTokenData> getAccessToken(String username, String password) {
        try (Keycloak keycloakLoginUserPassword =
                keycloakBuilder
                        .username(username)
                        .password(password)
                        .grantType(OAuth2Constants.PASSWORD)
                        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(100).build())
                        .build()) {
            return Optional.of(
                    accessTokenMapper.convertFromResponse(
                            keycloakLoginUserPassword.tokenManager().getAccessToken()));
        } catch (Exception e) {
            log.error("Failed to get access token for user {}: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    /** Creates a new user in Keycloak */
    @Override
    public UserData createUser(String username, String email, String password) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        // Check for duplicate username or email
        List<UserRepresentation> existingUsers =
                usersResource.search(username, null, null, email, 0, 1);
        if (!existingUsers.isEmpty()) {
            throw new KeycloakException(KeycloakErrorCode.USER_EXISTS);
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        // Set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                UserRepresentation createdUser = usersResource.get(userId).toRepresentation();
                List<RoleData> roles = getUserPermissions(userId);
                List<GroupData> groups = keycloakMapper.toGroupDataList(usersResource.get(userId).groups());
                log.info("Created user: {}", username);
                return keycloakMapper.toUserData(createdUser, roles, groups);
            }
            if (response.getStatus() == 409) {
                throw new KeycloakException(KeycloakErrorCode.USER_EXISTS);
            }
            throw new KeycloakException(KeycloakErrorCode.INVALID_REQUEST);
        }
    }

    /** Assigns a role to a user */
    @Override
    public OperationData assignPermissionToUser(String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        OperationData operationData = new OperationData();
        operationData.setUserId(userId);
        operationData.setRoleName(roleName);

        try {
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
            operationData.setSuccess(true);
            operationData.setMessage("Assigned role '" + roleName + "' to user successfully");
            log.info("Assigned role {} to user {}", roleName, userId);
        } catch (jakarta.ws.rs.NotFoundException e) {
            operationData.setSuccess(false);
            KeycloakErrorCode errorCode =
                    e.getMessage().contains("users")
                            ? KeycloakErrorCode.USER_NOT_FOUND
                            : KeycloakErrorCode.ROLE_NOT_FOUND;
            operationData.setMessage(errorCode.getMessage());
            throw new KeycloakException(errorCode);
        } catch (Exception e) {
            operationData.setSuccess(false);
            operationData.setMessage(KeycloakErrorCode.CANNOT_ASSIGN_ROLE_TO_USER.getMessage());
            throw new KeycloakException(KeycloakErrorCode.CANNOT_ASSIGN_ROLE_TO_USER);
        }
        return operationData;
    }

    /** Adds a user to a group */
    @Override
    public OperationData addUserToGroup(String userId, String groupId) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        OperationData operationData = new OperationData();
        operationData.setUserId(userId);
        operationData.setGroupId(groupId);

        try {
            realmResource.users().get(userId).joinGroup(groupId);
            operationData.setSuccess(true);
            operationData.setMessage("Added user to group successfully");
            log.info("Added user {} to group {}", userId, groupId);
        } catch (jakarta.ws.rs.NotFoundException e) {
            operationData.setSuccess(false);
            KeycloakErrorCode errorCode =
                    e.getMessage().contains("users")
                            ? KeycloakErrorCode.USER_NOT_FOUND
                            : KeycloakErrorCode.GROUP_NOT_FOUND;
            operationData.setMessage(errorCode.getMessage());
            throw new KeycloakException(errorCode);
        } catch (Exception e) {
            operationData.setSuccess(false);
            operationData.setMessage(KeycloakErrorCode.CANNOT_ADD_USER_TO_GROUP.getMessage());
            throw new KeycloakException(KeycloakErrorCode.CANNOT_ADD_USER_TO_GROUP);
        }
        return operationData;
    }

    /** Creates a new group in Keycloak */
    @Override
    public GroupData createGroup(String groupName) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupName);

        try (Response response = realmResource.groups().add(group)) {
            if (response.getStatus() == 201) {
                String groupId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                GroupRepresentation createdGroup = realmResource.groups().group(groupId).toRepresentation();
                log.info("Created group: {}", groupName);
                return keycloakMapper.toGroupData(createdGroup);
            }
            if (response.getStatus() == 409) {
                throw new KeycloakException(KeycloakErrorCode.GROUP_EXISTS);
            }
            throw new KeycloakException(KeycloakErrorCode.INVALID_REQUEST);
        }
    }

    /** Assigns a role to a group */
    @Override
    public OperationData assignPermissionToGroup(String groupId, String roleName) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        OperationData operationData = new OperationData();
        operationData.setGroupId(groupId);
        operationData.setRoleName(roleName);

        try {
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            realmResource
                    .groups()
                    .group(groupId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(role));
            operationData.setSuccess(true);
            operationData.setMessage("Assigned role '" + roleName + "' to group successfully");
            log.info("Assigned role {} to group {}", roleName, groupId);
        } catch (jakarta.ws.rs.NotFoundException e) {
            operationData.setSuccess(false);
            KeycloakErrorCode errorCode =
                    e.getMessage().contains("groups")
                            ? KeycloakErrorCode.GROUP_NOT_FOUND
                            : KeycloakErrorCode.ROLE_NOT_FOUND;
            operationData.setMessage(errorCode.getMessage());
            throw new KeycloakException(errorCode);
        } catch (Exception e) {
            operationData.setSuccess(false);
            operationData.setMessage(KeycloakErrorCode.CANNOT_ASSIGN_ROLE_TO_GROUP.getMessage());
            throw new KeycloakException(KeycloakErrorCode.CANNOT_ASSIGN_ROLE_TO_GROUP);
        }
        return operationData;
    }

    /** Gets list of user roles */
    @Override
    public List<RoleData> getUserPermissions(String userId) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        try {
            return keycloakMapper.toRoleDataList(
                    realmResource.users().get(userId).roles().realmLevel().listEffective());
        } catch (jakarta.ws.rs.NotFoundException e) {
            throw new KeycloakException(KeycloakErrorCode.USER_NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed to get roles for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /** Gets list of users with specific role */
    @Override
    public List<UserData> getUsersByPermission(String roleName) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        try {
            return realmResource.roles().get(roleName).getUserMembers().stream()
                    .map(
                            user -> {
                                UserRepresentation userRep =
                                        realmResource.users().get(user.getId()).toRepresentation();
                                List<RoleData> roles = getUserPermissions(userRep.getId());
                                List<GroupData> groups =
                                        keycloakMapper.toGroupDataList(
                                                realmResource.users().get(userRep.getId()).groups());
                                return keycloakMapper.toUserData(userRep, roles, groups);
                            })
                    .collect(Collectors.toList());
        } catch (jakarta.ws.rs.NotFoundException e) {
            throw new KeycloakException(KeycloakErrorCode.ROLE_NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed to get users with role {}: {}", roleName, e.getMessage());
            return Collections.emptyList();
        }
    }

    /** Removes a user from a group */
    @Override
    public OperationData removeUserFromGroup(String userId, String groupId) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        OperationData operationData = new OperationData();
        operationData.setUserId(userId);
        operationData.setGroupId(groupId);

        try {
            realmResource.users().get(userId).leaveGroup(groupId);
            operationData.setSuccess(true);
            operationData.setMessage("Removed user from group successfully");
            log.info("Removed user {} from group {}", userId, groupId);
        } catch (jakarta.ws.rs.NotFoundException e) {
            operationData.setSuccess(false);
            KeycloakErrorCode errorCode =
                    e.getMessage().contains("users")
                            ? KeycloakErrorCode.USER_NOT_FOUND
                            : KeycloakErrorCode.GROUP_NOT_FOUND;
            operationData.setMessage(errorCode.getMessage());
            throw new KeycloakException(errorCode);
        } catch (Exception e) {
            operationData.setSuccess(false);
            operationData.setMessage(KeycloakErrorCode.CANNOT_REMOVE_USER_FROM_GROUP.getMessage());
            throw new KeycloakException(KeycloakErrorCode.CANNOT_REMOVE_USER_FROM_GROUP);
        }
        return operationData;
    }
}
