package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.dto.request.GroupRequest;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.GroupDataAccessService;
import com.preschool.identityservice.infra.entity.GroupEntity;
import com.preschool.identityservice.infra.entity.RoleEntity;
import com.preschool.identityservice.infra.entity.UserEntity;
import com.preschool.identityservice.infra.mapper.GroupMapper;
import com.preschool.identityservice.infra.mapper.UserMapper;
import com.preschool.identityservice.infra.repository.GroupRepository;
import com.preschool.identityservice.infra.repository.RoleRepository;
import com.preschool.identityservice.infra.repository.UserRepository;
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
public class GroupServiceImpl implements GroupDataAccessService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    @Override
    public GroupData createGroup(GroupRequest request) {
        log.info("Creating group with name: {}", request.getGroupName());

        if (groupRepository.existsByGroupName(request.getGroupName())) {
            throw new RuntimeException("Group with name " + request.getGroupName() + " already exists");
        }

        GroupEntity groupEntity = groupMapper.toEntity(request);

        // Assign users if provided
        if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            List<UserEntity> users = userRepository.findAllById(request.getUserIds());
            groupEntity.getUsers().addAll(users);
        }

        // Assign roles if provided
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            List<RoleEntity> roles = roleRepository.findAllById(request.getRoleIds());
            groupEntity.getRoles().addAll(roles);
        }

        GroupEntity savedGroup = groupRepository.save(groupEntity);
        log.info("Group created successfully with ID: {}", savedGroup.getGroupId());

        return groupMapper.toData(savedGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupData getGroupById(UUID groupId) {
        log.info("Getting group by ID: {}", groupId);
        GroupEntity group =
                groupRepository
                        .findByIdWithUsersAndRoles(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));
        return groupMapper.toData(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupData getGroupByName(String groupName) {
        log.info("Getting group by name: {}", groupName);
        GroupEntity group =
                groupRepository
                        .findByGroupNameWithRoles(groupName)
                        .orElseThrow(() -> new RuntimeException("Group not found with name: " + groupName));
        return groupMapper.toData(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupData> getAllGroups() {
        log.info("Getting all groups");
        List<GroupEntity> groups = groupRepository.findAll();
        return groupMapper.toDataList(groups);
    }

    @Override
    public GroupData updateGroup(UUID groupId, GroupRequest request) {
        log.info("Updating group with ID: {}", groupId);

        GroupEntity existingGroup =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        if (!existingGroup.getGroupName().equals(request.getGroupName())
                && groupRepository.existsByGroupName(request.getGroupName())) {
            throw new RuntimeException("Group with name " + request.getGroupName() + " already exists");
        }

        groupMapper.updateEntityFromRequest(request, existingGroup);

        // Update users if provided
        if (request.getUserIds() != null) {
            existingGroup.getUsers().clear();
            if (!request.getUserIds().isEmpty()) {
                List<UserEntity> users = userRepository.findAllById(request.getUserIds());
                existingGroup.getUsers().addAll(users);
            }
        }

        // Update roles if provided
        if (request.getRoleIds() != null) {
            existingGroup.getRoles().clear();
            if (!request.getRoleIds().isEmpty()) {
                List<RoleEntity> roles = roleRepository.findAllById(request.getRoleIds());
                existingGroup.getRoles().addAll(roles);
            }
        }

        GroupEntity updatedGroup = groupRepository.save(existingGroup);
        log.info("Group updated successfully with ID: {}", updatedGroup.getGroupId());

        return groupMapper.toData(updatedGroup);
    }

    @Override
    public void deleteGroup(UUID groupId) {
        log.info("Deleting group with ID: {}", groupId);

        if (!groupRepository.existsById(groupId)) {
            throw new RuntimeException("Group not found with ID: " + groupId);
        }

        groupRepository.deleteById(groupId);
        log.info("Group deleted successfully with ID: {}", groupId);
    }

    @Override
    public void addUserToGroup(UUID userId, UUID groupId) {
        log.info("Adding user {} to group {}", userId, groupId);

        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        GroupEntity group =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        user.getGroups().add(group);
        userRepository.save(user);

        log.info("User added to group successfully");
    }

    @Override
    public void removeUserFromGroup(UUID userId, UUID groupId) {
        log.info("Removing user {} from group {}", userId, groupId);

        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.getGroups().removeIf(group -> group.getGroupId().equals(groupId));
        userRepository.save(user);

        log.info("User removed from group successfully");
    }

    @Override
    public void assignRoleToGroup(UUID groupId, UUID roleId) {
        log.info("Assigning role {} to group {}", roleId, groupId);

        GroupEntity group =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        RoleEntity role =
                roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        group.getRoles().add(role);
        groupRepository.save(group);

        log.info("Role assigned to group successfully");
    }

    @Override
    public void removeRoleFromGroup(UUID groupId, UUID roleId) {
        log.info("Removing role {} from group {}", roleId, groupId);

        GroupEntity group =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        group.getRoles().removeIf(role -> role.getRoleId().equals(roleId));
        groupRepository.save(group);

        log.info("Role removed from group successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserData> getUsersByGroup(UUID groupId) {
        log.info("Getting users by group ID: {}", groupId);

        GroupEntity group =
                groupRepository
                        .findByIdWithUsersAndRoles(groupId)
                        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        List<UserEntity> users = group.getUsers().stream().toList();
        return userMapper.toDataList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupData> getGroupsByUser(UUID userId) {
        log.info("Getting groups by user ID: {}", userId);

        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<GroupEntity> groups = user.getGroups().stream().toList();
        return groupMapper.toDataList(groups);
    }
}
