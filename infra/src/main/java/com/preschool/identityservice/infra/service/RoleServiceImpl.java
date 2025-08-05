package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.dto.request.RoleRequest;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.service.infra.RoleDataAccessService;
import com.preschool.identityservice.infra.entity.PermissionEntity;
import com.preschool.identityservice.infra.entity.RoleEntity;
import com.preschool.identityservice.infra.mapper.RoleMapper;
import com.preschool.identityservice.infra.repository.PermissionRepository;
import com.preschool.identityservice.infra.repository.RoleRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleDataAccessService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleData createRole(RoleRequest request) {
        log.info("Creating role with name: {}", request.getRoleName());

        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role with name " + request.getRoleName() + " already exists");
        }

        RoleEntity roleEntity = roleMapper.toEntity(request);

        // Assign permissions if provided
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            List<PermissionEntity> permissions =
                    permissionRepository.findAllById(request.getPermissionIds());
            roleEntity.getPermissions().addAll(permissions);
        }

        RoleEntity savedRole = roleRepository.save(roleEntity);
        log.info("Role created successfully with ID: {}", savedRole.getRoleId());

        return roleMapper.toData(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleData getRoleById(UUID roleId) {
        log.info("Getting role by ID: {}", roleId);
        RoleEntity role =
                roleRepository
                        .findByIdWithPermissions(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        return roleMapper.toData(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleData getRoleByName(String roleName) {
        log.info("Getting role by name: {}", roleName);
        RoleEntity role =
                roleRepository
                        .findByRoleNameWithPermissions(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
        return roleMapper.toData(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleData> getAllRoles() {
        log.info("Getting all roles");
        List<RoleEntity> roles = roleRepository.findAll();
        return roleMapper.toDataList(roles);
    }

    @Override
    public RoleData updateRole(UUID roleId, RoleRequest request) {
        log.info("Updating role with ID: {}", roleId);

        RoleEntity existingRole =
                roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        if (!existingRole.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role with name " + request.getRoleName() + " already exists");
        }

        roleMapper.updateEntityFromRequest(request, existingRole);

        // Update permissions if provided
        if (request.getPermissionIds() != null) {
            existingRole.getPermissions().clear();
            if (!request.getPermissionIds().isEmpty()) {
                List<PermissionEntity> permissions =
                        permissionRepository.findAllById(request.getPermissionIds());
                existingRole.getPermissions().addAll(permissions);
            }
        }

        RoleEntity updatedRole = roleRepository.save(existingRole);
        log.info("Role updated successfully with ID: {}", updatedRole.getRoleId());

        return roleMapper.toData(updatedRole);
    }

    @Override
    public void deleteRole(UUID roleId) {
        log.info("Deleting role with ID: {}", roleId);

        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }

        roleRepository.deleteById(roleId);
        log.info("Role deleted successfully with ID: {}", roleId);
    }

    @Override
    public void assignPermissionToRole(UUID roleId, UUID permissionId) {
        log.info("Assigning permission {} to role {}", permissionId, roleId);

        RoleEntity role =
                roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        PermissionEntity permission =
                permissionRepository
                        .findById(permissionId)
                        .orElseThrow(
                                () -> new RuntimeException("Permission not found with ID: " + permissionId));

        role.getPermissions().add(permission);
        roleRepository.save(role);

        log.info("Permission assigned successfully");
    }

    @Override
    public void removePermissionFromRole(UUID roleId, UUID permissionId) {
        log.info("Removing permission {} from role {}", permissionId, roleId);

        RoleEntity role =
                roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        role.getPermissions().removeIf(permission -> permission.getPermissionId().equals(permissionId));
        roleRepository.save(role);

        log.info("Permission removed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleData> getRolesByPermission(UUID permissionId) {
        log.info("Getting roles by permission ID: {}", permissionId);

        // This can be optimized with a custom query if needed
        List<RoleEntity> roles = roleRepository.findAll();
        List<RoleEntity> filteredRoles =
                roles.stream()
                        .filter(
                                role ->
                                        role.getPermissions().stream()
                                                .anyMatch(permission -> permission.getPermissionId().equals(permissionId)))
                        .toList();

        return roleMapper.toDataList(filteredRoles);
    }
}
