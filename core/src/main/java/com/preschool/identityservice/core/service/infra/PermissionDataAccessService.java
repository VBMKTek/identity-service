package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.common.param.PermissionParam;
import com.preschool.identityservice.core.data.PermissionData;
import java.util.List;
import java.util.UUID;

/**
 * Interface for Permission data access operations This provides abstraction for data access layer
 * following clean architecture
 */
public interface PermissionDataAccessService {

    PermissionData createPermission(PermissionParam param);

    PermissionData getPermissionById(UUID permissionId);

    PermissionData getPermissionByName(String permissionName);

    List<PermissionData> getAllPermissions();

    PermissionData updatePermission(UUID permissionId, PermissionParam param);

    void deletePermission(UUID permissionId);

    List<PermissionData> getPermissionsByRole(UUID roleId);
}
