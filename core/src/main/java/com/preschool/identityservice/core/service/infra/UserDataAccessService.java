package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.common.param.UserParam;
import com.preschool.identityservice.core.data.UserData;
import java.util.List;
import java.util.UUID;

/**
 * Interface for User database operations This provides abstraction for data access layer following
 * clean architecture
 */
public interface UserDataAccessService {

    UserData createUser(UserParam param);

    UserData getUserById(UUID userId);

    UserData getUserByKeycloakId(String keycloakId);

    UserData getUserByUsername(String username);

    List<UserData> getAllUsers();

    UserData updateUser(UUID userId, UserParam param);

    void deleteUser(UUID userId);

    UserData syncUserFromKeycloak(String keycloakId, String username, String email);

    void assignRoleToUser(UUID userId, UUID roleId);

    void removeRoleFromUser(UUID userId, UUID roleId);

    List<UserData> getUsersByRole(UUID roleId);
}
