package com.preschool.identityservice.api.controller.admin;

import com.preschool.identityservice.api.dto.request.*;
import com.preschool.identityservice.api.dto.response.GroupResponse;
import com.preschool.identityservice.api.dto.response.OperationResponse;
import com.preschool.identityservice.api.dto.response.RoleResponse;
import com.preschool.identityservice.api.dto.response.UserResponse;
import com.preschool.identityservice.api.mapper.ApiKeycloakMapper;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.OperationData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.AuthenticationService;
import com.preschool.libraries.base.response.Response;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Controller for admin authentication operations */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/authentication")
public class AdminAuthenticationController {
    private final AuthenticationService authenticationService;
    private final ApiKeycloakMapper apiKeycloakMapper;

    /** Creates a new user */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/users")
    public ResponseEntity<Response<UserResponse>> createUser(
            @RequestBody @Valid CreateUserRequest request) {
        UserData userData =
                authenticationService.createUser(
                        request.getUsername(), request.getEmail(), request.getPassword());
        UserResponse userResponse = apiKeycloakMapper.toUserResponse(userData);
        return ResponseEntity.ok(Response.success(userResponse));
    }

    /** Assigns a role to a user */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/users/permissions")
    public ResponseEntity<Response<OperationResponse>> assignPermissionToUser(
            @RequestBody @Valid AssignUserPermissionRequest request) {
        OperationData operationData =
                authenticationService.assignPermissionToUser(request.getUserId(), request.getRoleName());
        OperationResponse operationResponse = apiKeycloakMapper.toOperationResponse(operationData);
        return ResponseEntity.ok(Response.success(operationResponse));
    }

    /** Adds a user to a group */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/users/groups")
    public ResponseEntity<Response<OperationResponse>> addUserToGroup(
            @RequestBody @Valid AssignGroupRequest request) {
        OperationData operationData =
                authenticationService.addUserToGroup(request.getUserId(), request.getGroupId());
        OperationResponse operationResponse = apiKeycloakMapper.toOperationResponse(operationData);
        return ResponseEntity.ok(Response.success(operationResponse));
    }

    /** Creates a new group */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/groups")
    public ResponseEntity<Response<GroupResponse>> createGroup(
            @RequestBody @Valid CreateGroupRequest request) {
        String groupName = apiKeycloakMapper.toCreateGroupParam(request);
        GroupData groupData = authenticationService.createGroup(groupName);
        GroupResponse groupResponse = apiKeycloakMapper.toGroupResponse(groupData);
        return ResponseEntity.ok(Response.success(groupResponse));
    }

    /** Assigns a role to a group */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/groups/permissions")
    public ResponseEntity<Response<OperationResponse>> assignPermissionToGroup(
            @RequestBody @Valid AssignGroupPermissionRequest request) {
        OperationData operationData =
                authenticationService.assignPermissionToGroup(request.getGroupId(), request.getRoleName());
        OperationResponse operationResponse = apiKeycloakMapper.toOperationResponse(operationData);
        return ResponseEntity.ok(Response.success(operationResponse));
    }

    /** Retrieves permissions for a user */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<Response<List<RoleResponse>>> getUserPermissions(
            @PathVariable String userId) {
        List<RoleResponse> roles =
                authenticationService.getUserPermissions(userId).stream()
                        .map(apiKeycloakMapper::toRoleResponse)
                        .toList();
        return ResponseEntity.ok(Response.success(roles));
    }

    /** Retrieves users with a specific role */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/permissions/{roleName}/users")
    public ResponseEntity<Response<List<UserResponse>>> getUsersByPermission(
            @PathVariable String roleName) {
        List<UserResponse> users =
                authenticationService.getUsersByPermission(roleName).stream()
                        .map(apiKeycloakMapper::toUserResponse)
                        .toList();
        return ResponseEntity.ok(Response.success(users));
    }

    /** Removes a user from a group */
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/users/groups")
    public ResponseEntity<Response<OperationResponse>> removeUserFromGroup(
            @RequestBody @Valid AssignGroupRequest request) {
        OperationData operationData =
                authenticationService.removeUserFromGroup(request.getUserId(), request.getGroupId());
        OperationResponse operationResponse = apiKeycloakMapper.toOperationResponse(operationData);
        return ResponseEntity.ok(Response.success(operationResponse));
    }
}
