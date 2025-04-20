package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.core.data.*;
import java.util.List;
import java.util.Optional;

public interface KeycloakService {
    Optional<AccessTokenData> getAccessToken(String username, String password);

    UserData createUser(String username, String email, String password);

    OperationData assignPermissionToUser(String userId, String roleName);

    OperationData addUserToGroup(String userId, String groupId);

    GroupData createGroup(String groupName);

    OperationData assignPermissionToGroup(String groupId, String roleName);

    List<RoleData> getUserPermissions(String userId);

    List<UserData> getUsersByPermission(String roleName);

    OperationData removeUserFromGroup(String userId, String groupId);
}
