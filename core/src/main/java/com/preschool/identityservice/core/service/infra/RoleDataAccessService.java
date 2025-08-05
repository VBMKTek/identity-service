package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.common.param.RoleParam;
import com.preschool.identityservice.core.data.RoleData;
import java.util.List;
import java.util.UUID;

/**
 * Interface for Role data access operations This provides abstraction for data access layer
 * following clean architecture
 */
public interface RoleDataAccessService {

    RoleData createRole(RoleParam param);

    RoleData getRoleById(UUID roleId);

    RoleData getRoleByName(String roleName);

    List<RoleData> getAllRoles();

    RoleData updateRole(UUID roleId, RoleParam param);

    void deleteRole(UUID roleId);

    void assignPermissionToRole(UUID roleId, UUID permissionId);

    void removePermissionFromRole(UUID roleId, UUID permissionId);

    List<RoleData> getRolesByPermission(UUID permissionId);
}
