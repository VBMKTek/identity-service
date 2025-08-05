package com.preschool.identityservice.core.service;

import com.preschool.identityservice.common.exception.InvalidDataException;
import com.preschool.identityservice.common.param.PermissionParam;
import com.preschool.identityservice.core.data.PermissionData;
import com.preschool.identityservice.core.service.infra.PermissionDataAccessService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Business logic service for Permission operations Contains all business rules and validation for
 * permission management
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PermissionBusinessService {

    private final PermissionDataAccessService permissionDataAccessService;

    public PermissionData createPermission(PermissionParam param) {
        log.info("Creating permission with name: {}", param.getPermissionName());

        // Business validation
        validatePermissionCreation(param);

        PermissionData result = permissionDataAccessService.createPermission(param);
        log.info("Permission created successfully with ID: {}", result.getPermissionId());

        return result;
    }

    @Transactional(readOnly = true)
    public PermissionData getPermissionById(UUID permissionId) {
        log.info("Getting permission by ID: {}", permissionId);
        return permissionDataAccessService.getPermissionById(permissionId);
    }

    @Transactional(readOnly = true)
    public PermissionData getPermissionByName(String permissionName) {
        log.info("Getting permission by name: {}", permissionName);
        return permissionDataAccessService.getPermissionByName(permissionName);
    }

    @Transactional(readOnly = true)
    public List<PermissionData> getAllPermissions() {
        log.info("Getting all permissions");
        return permissionDataAccessService.getAllPermissions();
    }

    public PermissionData updatePermission(UUID permissionId, PermissionParam param) {
        log.info("Updating permission with ID: {}", permissionId);

        // Business validation
        validatePermissionUpdate(param);

        PermissionData result = permissionDataAccessService.updatePermission(permissionId, param);
        log.info("Permission updated successfully with ID: {}", permissionId);

        return result;
    }

    public void deletePermission(UUID permissionId) {
        log.info("Deleting permission with ID: {}", permissionId);

        // Business validation - check if permission is being used
        validatePermissionDeletion(permissionId);

        permissionDataAccessService.deletePermission(permissionId);
        log.info("Permission deleted successfully with ID: {}", permissionId);
    }

    @Transactional(readOnly = true)
    public List<PermissionData> getPermissionsByRole(UUID roleId) {
        log.info("Getting permissions by role ID: {}", roleId);
        return permissionDataAccessService.getPermissionsByRole(roleId);
    }

    // Private helper methods for business logic
    private void validatePermissionCreation(PermissionParam param) {
        if (!StringUtils.hasText(param.getPermissionName())) {
            throw new InvalidDataException("Permission name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Permission description cannot be empty");
        }

        // Add more business rules as needed
        if (param.getPermissionName().length() < 3) {
            throw new InvalidDataException("Permission name must be at least 3 characters long");
        }
    }

    private void validatePermissionUpdate(PermissionParam param) {
        if (!StringUtils.hasText(param.getPermissionName())) {
            throw new InvalidDataException("Permission name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Permission description cannot be empty");
        }
    }

    private void validatePermissionDeletion(UUID permissionId) {
        // Check if permission is assigned to any roles
        List<PermissionData> rolesUsingPermission =
                permissionDataAccessService.getPermissionsByRole(permissionId);
        if (!rolesUsingPermission.isEmpty()) {
            throw new InvalidDataException("Cannot delete permission that is assigned to roles");
        }
    }
}
