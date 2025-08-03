package com.preschool.identityservice.core.service;

import com.preschool.identityservice.common.param.GroupParam;
import com.preschool.identityservice.common.exception.InvalidDataException;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.GroupDataAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Business logic service for Group operations
 * Contains all business rules and validation for group management
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GroupBusinessService {

    private final GroupDataAccessService groupDataAccessService;

    public GroupData createGroup(GroupParam param) {
        log.info("Creating group with name: {}", param.getGroupName());
        
        // Business validation
        validateGroupCreation(param);
        
        GroupData result = groupDataAccessService.createGroup(param);
        log.info("Group created successfully with ID: {}", result.getGroupId());
        
        return result;
    }

    @Transactional(readOnly = true)
    public GroupData getGroupById(UUID groupId) {
        log.info("Getting group by ID: {}", groupId);
        return groupDataAccessService.getGroupById(groupId);
    }

    @Transactional(readOnly = true)
    public GroupData getGroupByName(String groupName) {
        log.info("Getting group by name: {}", groupName);
        return groupDataAccessService.getGroupByName(groupName);
    }

    @Transactional(readOnly = true)
    public List<GroupData> getAllGroups() {
        log.info("Getting all groups");
        return groupDataAccessService.getAllGroups();
    }

    public GroupData updateGroup(UUID groupId, GroupParam param) {
        log.info("Updating group with ID: {}", groupId);
        
        // Business validation
        validateGroupUpdate(param);
        
        GroupData result = groupDataAccessService.updateGroup(groupId, param);
        log.info("Group updated successfully with ID: {}", groupId);
        
        return result;
    }

    public void deleteGroup(UUID groupId) {
        log.info("Deleting group with ID: {}", groupId);
        
        // Business validation - check if group has users
        validateGroupDeletion(groupId);
        
        groupDataAccessService.deleteGroup(groupId);
        log.info("Group deleted successfully with ID: {}", groupId);
    }

    public void addUserToGroup(UUID userId, UUID groupId) {
        log.info("Adding user {} to group {}", userId, groupId);
        
        // Business validation
        validateUserGroupOperation(userId, groupId);
        
        groupDataAccessService.addUserToGroup(userId, groupId);
        log.info("User added to group successfully");
    }

    public void removeUserFromGroup(UUID userId, UUID groupId) {
        log.info("Removing user {} from group {}", userId, groupId);
        
        groupDataAccessService.removeUserFromGroup(userId, groupId);
        log.info("User removed from group successfully");
    }

    public void assignRoleToGroup(UUID groupId, UUID roleId) {
        log.info("Assigning role {} to group {}", roleId, groupId);
        
        // Business validation
        validateRoleGroupOperation(groupId, roleId);
        
        groupDataAccessService.assignRoleToGroup(groupId, roleId);
        log.info("Role assigned to group successfully");
    }

    public void removeRoleFromGroup(UUID groupId, UUID roleId) {
        log.info("Removing role {} from group {}", roleId, groupId);
        
        groupDataAccessService.removeRoleFromGroup(groupId, roleId);
        log.info("Role removed from group successfully");
    }

    @Transactional(readOnly = true)
    public List<UserData> getUsersByGroup(UUID groupId) {
        log.info("Getting users by group ID: {}", groupId);
        return groupDataAccessService.getUsersByGroup(groupId);
    }

    @Transactional(readOnly = true)
    public List<GroupData> getGroupsByUser(UUID userId) {
        log.info("Getting groups by user ID: {}", userId);
        return groupDataAccessService.getGroupsByUser(userId);
    }

    // Private helper methods for business logic
    private void validateGroupCreation(GroupParam param) {
        if (!StringUtils.hasText(param.getGroupName())) {
            throw new InvalidDataException("Group name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Group description cannot be empty");
        }
        
        // Add more business rules as needed
        if (param.getGroupName().length() < 3) {
            throw new InvalidDataException("Group name must be at least 3 characters long");
        }
    }

    private void validateGroupUpdate(GroupParam param) {
        if (!StringUtils.hasText(param.getGroupName())) {
            throw new InvalidDataException("Group name cannot be empty");
        }
        if (!StringUtils.hasText(param.getDescription())) {
            throw new InvalidDataException("Group description cannot be empty");
        }
    }

    private void validateGroupDeletion(UUID groupId) {
        // Check if group has users
        List<UserData> usersInGroup = groupDataAccessService.getUsersByGroup(groupId);
        if (!usersInGroup.isEmpty()) {
            throw new InvalidDataException("Cannot delete group that contains users. Remove all users first.");
        }
    }

    private void validateUserGroupOperation(UUID userId, UUID groupId) {
        if (userId == null) {
            throw new InvalidDataException("User ID cannot be null");
        }
        if (groupId == null) {
            throw new InvalidDataException("Group ID cannot be null");
        }
    }

    private void validateRoleGroupOperation(UUID groupId, UUID roleId) {
        if (groupId == null) {
            throw new InvalidDataException("Group ID cannot be null");
        }
        if (roleId == null) {
            throw new InvalidDataException("Role ID cannot be null");
        }
    }
}
