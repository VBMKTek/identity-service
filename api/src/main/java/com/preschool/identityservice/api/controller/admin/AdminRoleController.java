package com.preschool.identityservice.api.controller.admin;

import com.preschool.identityservice.api.dto.request.RoleRequest;
import com.preschool.identityservice.api.mapper.RoleMapper;
import com.preschool.identityservice.common.param.RoleParam;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.service.RoleBusinessService;
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
@RequestMapping("/v1/admin/roles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

    private final RoleBusinessService roleBusinessService;
    private final RoleMapper roleMapper;

    @PostMapping
    public ResponseEntity<RoleData> createRole(@Valid @RequestBody RoleRequest request) {
        log.info("Admin creating role: {}", request.getRoleName());
        RoleParam param = roleMapper.requestToParam(request);
        RoleData roleData = roleBusinessService.createRole(param);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleData);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleData> getRoleById(@PathVariable UUID roleId) {
        log.info("Admin getting role by ID: {}", roleId);
        RoleData roleData = roleBusinessService.getRoleById(roleId);
        return ResponseEntity.ok(roleData);
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleData> getRoleByName(@PathVariable String roleName) {
        log.info("Admin getting role by name: {}", roleName);
        RoleData roleData = roleBusinessService.getRoleByName(roleName);
        return ResponseEntity.ok(roleData);
    }

    @GetMapping
    public ResponseEntity<List<RoleData>> getAllRoles() {
        log.info("Admin getting all roles");
        List<RoleData> roles = roleBusinessService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleData> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleRequest request) {
        log.info("Admin updating role: {}", roleId);
        RoleParam param = roleMapper.requestToParam(request);
        RoleData roleData = roleBusinessService.updateRole(roleId, param);
        return ResponseEntity.ok(roleData);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        log.info("Admin deleting role: {}", roleId);
        roleBusinessService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> assignPermissionToRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        log.info("Admin assigning permission {} to role {}", permissionId, roleId);
        roleBusinessService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        log.info("Admin removing permission {} from role {}", permissionId, roleId);
        roleBusinessService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-permission/{permissionId}")
    public ResponseEntity<List<RoleData>> getRolesByPermission(@PathVariable UUID permissionId) {
        log.info("Admin getting roles by permission: {}", permissionId);
        List<RoleData> roles = roleBusinessService.getRolesByPermission(permissionId);
        return ResponseEntity.ok(roles);
    }

}
