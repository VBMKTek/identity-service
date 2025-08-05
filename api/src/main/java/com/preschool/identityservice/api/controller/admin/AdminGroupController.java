package com.preschool.identityservice.api.controller.admin;

import com.preschool.identityservice.api.dto.request.GroupRequest;
import com.preschool.identityservice.api.mapper.GroupMapper;
import com.preschool.identityservice.common.param.GroupParam;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.GroupBusinessService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/groups")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminGroupController {

    private final GroupBusinessService groupBusinessService;
    private final GroupMapper groupMapper;

    @PostMapping
    public ResponseEntity<GroupData> createGroup(@Valid @RequestBody GroupRequest request) {
        log.info("Admin creating group: {}", request.getGroupName());
        GroupParam param = groupMapper.requestToParam(request);
        GroupData groupData = groupBusinessService.createGroup(param);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupData);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupData> getGroupById(@PathVariable UUID groupId) {
        log.info("Admin getting group by ID: {}", groupId);
        GroupData groupData = groupBusinessService.getGroupById(groupId);
        return ResponseEntity.ok(groupData);
    }

    @GetMapping("/name/{groupName}")
    public ResponseEntity<GroupData> getGroupByName(@PathVariable String groupName) {
        log.info("Admin getting group by name: {}", groupName);
        GroupData groupData = groupBusinessService.getGroupByName(groupName);
        return ResponseEntity.ok(groupData);
    }

    @GetMapping
    public ResponseEntity<List<GroupData>> getAllGroups() {
        log.info("Admin getting all groups");
        List<GroupData> groups = groupBusinessService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupData> updateGroup(
            @PathVariable UUID groupId, @Valid @RequestBody GroupRequest request) {
        log.info("Admin updating group: {}", groupId);
        GroupParam param = groupMapper.requestToParam(request);
        GroupData groupData = groupBusinessService.updateGroup(groupId, param);
        return ResponseEntity.ok(groupData);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
        log.info("Admin deleting group: {}", groupId);
        groupBusinessService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> addUserToGroup(
            @PathVariable UUID groupId, @PathVariable UUID userId) {
        log.info("Admin adding user {} to group {}", userId, groupId);
        groupBusinessService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(
            @PathVariable UUID groupId, @PathVariable UUID userId) {
        log.info("Admin removing user {} from group {}", userId, groupId);
        groupBusinessService.removeUserFromGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToGroup(
            @PathVariable UUID groupId, @PathVariable UUID roleId) {
        log.info("Admin assigning role {} to group {}", roleId, groupId);
        groupBusinessService.assignRoleToGroup(groupId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromGroup(
            @PathVariable UUID groupId, @PathVariable UUID roleId) {
        log.info("Admin removing role {} from group {}", roleId, groupId);
        groupBusinessService.removeRoleFromGroup(groupId, roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserData>> getUsersByGroup(@PathVariable UUID groupId) {
        log.info("Admin getting users by group: {}", groupId);
        List<UserData> users = groupBusinessService.getUsersByGroup(groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupData>> getGroupsByUser(@PathVariable UUID userId) {
        log.info("Admin getting groups by user: {}", userId);
        List<GroupData> groups = groupBusinessService.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }
}
