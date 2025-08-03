package com.preschool.identityservice.api.controller;

import com.preschool.identityservice.api.dto.request.PermissionRequest;
import com.preschool.identityservice.api.mapper.PermissionMapper;
import com.preschool.identityservice.common.param.PermissionParam;
import com.preschool.identityservice.core.data.PermissionData;
import com.preschool.identityservice.core.service.PermissionBusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminPermissionController {

    private final PermissionBusinessService permissionBusinessService;
    private final PermissionMapper permissionMapper;

    @PostMapping
    public ResponseEntity<PermissionData> createPermission(@Valid @RequestBody PermissionRequest request) {
        log.info("Admin creating permission: {}", request.getPermissionName());
        PermissionParam param = permissionMapper.requestToParam(request);
        PermissionData permissionData = permissionBusinessService.createPermission(param);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionData);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionData> getPermissionById(@PathVariable UUID permissionId) {
        log.info("Admin getting permission by ID: {}", permissionId);
        PermissionData permissionData = permissionBusinessService.getPermissionById(permissionId);
        return ResponseEntity.ok(permissionData);
    }

    @GetMapping("/name/{permissionName}")
    public ResponseEntity<PermissionData> getPermissionByName(@PathVariable String permissionName) {
        log.info("Admin getting permission by name: {}", permissionName);
        PermissionData permissionData = permissionBusinessService.getPermissionByName(permissionName);
        return ResponseEntity.ok(permissionData);
    }

    @GetMapping
    public ResponseEntity<List<PermissionData>> getAllPermissions() {
        log.info("Admin getting all permissions");
        List<PermissionData> permissions = permissionBusinessService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionData> updatePermission(
            @PathVariable UUID permissionId,
            @Valid @RequestBody PermissionRequest request) {
        log.info("Admin updating permission: {}", permissionId);
        PermissionParam param = permissionMapper.requestToParam(request);
        PermissionData permissionData = permissionBusinessService.updatePermission(permissionId, param);
        return ResponseEntity.ok(permissionData);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID permissionId) {
        log.info("Admin deleting permission: {}", permissionId);
        permissionBusinessService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<PermissionData>> getPermissionsByRole(@PathVariable UUID roleId) {
        log.info("Admin getting permissions by role: {}", roleId);
        List<PermissionData> permissions = permissionBusinessService.getPermissionsByRole(roleId);
        return ResponseEntity.ok(permissions);
    }
}
