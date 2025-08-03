package com.preschool.identityservice.api.controller.admin;

import com.preschool.identityservice.api.dto.request.UserRequest;
import com.preschool.identityservice.api.mapper.UserMapper;
import com.preschool.identityservice.common.param.UserParam;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.UserBusinessService;
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
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserBusinessService userBusinessService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserRequest request) {
        log.info("Admin creating user: {}", request.getUsername());
        UserParam param = userMapper.requestToParam(request);
        UserData userData = userBusinessService.createUser(param);
        return ResponseEntity.status(HttpStatus.CREATED).body(userData);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserData> getUserById(@PathVariable UUID userId) {
        log.info("Admin getting user by ID: {}", userId);
        UserData userData = userBusinessService.getUserById(userId);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserData> getUserByUsername(@PathVariable String username) {
        log.info("Admin getting user by username: {}", username);
        UserData userData = userBusinessService.getUserByUsername(username);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<UserData> getUserByKeycloakId(@PathVariable String keycloakId) {
        log.info("Admin getting user by Keycloak ID: {}", keycloakId);
        UserData userData = userBusinessService.getUserByKeycloakId(keycloakId);
        return ResponseEntity.ok(userData);
    }

    @GetMapping
    public ResponseEntity<List<UserData>> getAllUsers() {
        log.info("Admin getting all users");
        List<UserData> users = userBusinessService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserData> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserRequest request) {
        log.info("Admin updating user: {}", userId);
        UserParam param = userMapper.requestToParam(request);
        UserData userData = userBusinessService.updateUser(userId, param);
        return ResponseEntity.ok(userData);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("Admin deleting user: {}", userId);
        userBusinessService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        log.info("Admin assigning role {} to user {}", roleId, userId);
        userBusinessService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        log.info("Admin removing role {} from user {}", roleId, userId);
        userBusinessService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<UserData>> getUsersByRole(@PathVariable UUID roleId) {
        log.info("Admin getting users by role: {}", roleId);
        List<UserData> users = userBusinessService.getUsersByRole(roleId);
        return ResponseEntity.ok(users);
    }
}
