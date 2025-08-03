package com.preschool.identityservice.core.service;

import com.preschool.identityservice.common.param.RoleParam;
import com.preschool.identityservice.common.exception.InvalidDataException;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.service.infra.RoleDataAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Business logic service for Role operations
 * Contains all business rules and validation for role management
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleBusinessService {

    private final RoleDataAccessService roleDataAccessService;

    public RoleData createRole(RoleParam param) {
        log.info("Creating role with name: {}", param.getRoleName());
        
        // Business validation
        validateRoleCreation(param);
        
        RoleData result = roleDataAccessService.createRole(param);
        log.info("Role created successfully with ID: {}", result.getRoleId());
        
        return result;
    }

    @Transactional(readOnly = true)
    public RoleData getRoleById(UUID roleId) {
        log.info("Getting role by ID: {}", roleId);
        return roleDataAccessService.getRoleById(roleId);
    }

    @Transactional(readOnly = true)
    public RoleData getRoleByName(String roleName) {
        log.info("Getting role by name: {}", roleName);
        return roleDataAccessService.getRoleByName(roleName);
    }

    @Transactional(readOnly = true)
    public List<RoleData> getAllRoles() {
        log.info("Getting all roles");
        return roleDataAccessService.getAllRoles();
    }

    public RoleData updateRole(UUID roleId, RoleParam param) {
        log.info("Updating role with ID: {}", roleId);
        
        // Business validation
        validateRoleUpdate(param);
        
        RoleData result = roleDataAccessService.updateRole(roleId, param);
        log.info("Role updated successfully with ID: {}", roleId);
        
        return result;
    }

    public void deleteRole(UUID roleId) {
        log.info("Deleting role with ID: {}", roleId);
        
        // Business validation - check if role is being used
        validateRoleDeletion(roleId);
        
        roleDataAccessService.deleteRole(roleId);
        log.info("Role deleted successfully with ID: {}", roleId);
    }

    public void assignPermissionToRole(UUID roleId, UUID permissionId) {
        log.info("Assigning permission {} to role {}", permissionId, roleId);
        
        // Business validation
        validatePermissionAssignment(roleId, permissionId);
        
        roleDataAccessService.assignPermissionToRole(roleId, permissionId);
        log.info("Permission assigned successfully");
    }

    public void removePermissionFromRole(UUID roleId, UUID permissionId) {
        log.info("Removing permission {} from role {}", permissionId, roleId);
        
        roleDataAccessService.removePermissionFromRole(roleId, permissionId);
        log.info("Permission removed successfully");
    }

    @Transactional(readOnly = true)
    public List<RoleData> getRolesByPermission(UUID permissionId) {
        log.info("Getting roles by permission ID: {}", permissionId);
        return roleDataAccessService.getRolesByPermission(permissionId);
    }

    // Private helper methods for business logic
    private void validateRoleCreation(RoleParam param) {
        if (!StringUtils.hasText(param.getRoleName())) {
            throw new InvalidDataException("Role name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Role description cannot be empty");
        }
        
        // Add more business rules as needed
        if (param.getRoleName().length() < 3) {
            throw new InvalidDataException("Role name must be at least 3 characters long");
        }
    }

    private void validateRoleUpdate(RoleParam param) {
        if (!StringUtils.hasText(param.getRoleName())) {
            throw new InvalidDataException("Role name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Role description cannot be empty");
        }
    }

    private void validateRoleDeletion(UUID roleId) {
        // Add business rules for role deletion
        // For example, check if role is assigned to users
        log.debug("Validating role deletion for ID: {}", roleId);
    }

    private void validatePermissionAssignment(UUID roleId, UUID permissionId) {
        if (roleId == null) {
            throw new InvalidDataException("Role ID cannot be null");
        }
        if (permissionId == null) {
            throw new InvalidDataException("Permission ID cannot be null");
        }
    }
}
