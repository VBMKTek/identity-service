package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.param.PermissionParam;
import com.preschool.identityservice.core.data.PermissionData;
import com.preschool.identityservice.core.service.infra.PermissionDataAccessService;
import com.preschool.identityservice.infra.entity.PermissionEntity;
import com.preschool.identityservice.infra.mapper.PermissionMapper;
import com.preschool.identityservice.infra.repository.PermissionRepository;
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
public class PermissionServiceImpl implements PermissionDataAccessService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionData createPermission(PermissionParam request) {
        log.info("Creating permission with name: {}", request.getPermissionName());

        if (permissionRepository.existsByPermissionName(request.getPermissionName())) {
            throw new RuntimeException(
                    "Permission with name " + request.getPermissionName() + " already exists");
        }

        PermissionEntity permissionEntity = permissionMapper.toEntity(request);
        PermissionEntity savedPermission = permissionRepository.save(permissionEntity);

        log.info("Permission created successfully with ID: {}", savedPermission.getPermissionId());
        return permissionMapper.toData(savedPermission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionData getPermissionById(UUID permissionId) {
        log.info("Getting permission by ID: {}", permissionId);
        PermissionEntity permission =
                permissionRepository
                        .findById(permissionId)
                        .orElseThrow(
                                () -> new RuntimeException("Permission not found with ID: " + permissionId));
        return permissionMapper.toData(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionData getPermissionByName(String permissionName) {
        log.info("Getting permission by name: {}", permissionName);
        PermissionEntity permission =
                permissionRepository
                        .findByPermissionName(permissionName)
                        .orElseThrow(
                                () -> new RuntimeException("Permission not found with name: " + permissionName));
        return permissionMapper.toData(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionData> getAllPermissions() {
        log.info("Getting all permissions");
        List<PermissionEntity> permissions = permissionRepository.findAll();
        return permissionMapper.toDataList(permissions);
    }

    @Override
    public PermissionData updatePermission(UUID permissionId, PermissionParam request) {
        log.info("Updating permission with ID: {}", permissionId);

        PermissionEntity existingPermission =
                permissionRepository
                        .findById(permissionId)
                        .orElseThrow(
                                () -> new RuntimeException("Permission not found with ID: " + permissionId));

        if (!existingPermission.getPermissionName().equals(request.getPermissionName())
                && permissionRepository.existsByPermissionName(request.getPermissionName())) {
            throw new RuntimeException(
                    "Permission with name " + request.getPermissionName() + " already exists");
        }

        permissionMapper.updateEntityFromRequest(request, existingPermission);
        PermissionEntity updatedPermission = permissionRepository.save(existingPermission);

        log.info("Permission updated successfully with ID: {}", updatedPermission.getPermissionId());
        return permissionMapper.toData(updatedPermission);
    }

    @Override
    public void deletePermission(UUID permissionId) {
        log.info("Deleting permission with ID: {}", permissionId);

        if (!permissionRepository.existsById(permissionId)) {
            throw new RuntimeException("Permission not found with ID: " + permissionId);
        }

        permissionRepository.deleteById(permissionId);
        log.info("Permission deleted successfully with ID: {}", permissionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionData> getPermissionsByRole(UUID roleId) {
        log.info("Getting permissions by role ID: {}", roleId);

        // This can be optimized with a custom query if needed
        List<PermissionEntity> permissions = permissionRepository.findAll();
        List<PermissionEntity> filteredPermissions =
                permissions.stream()
                        .filter(
                                permission ->
                                        permission.getRoles().stream()
                                                .anyMatch(role -> role.getRoleId().equals(roleId)))
                        .toList();

        return permissionMapper.toDataList(filteredPermissions);
    }
}
