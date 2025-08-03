package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.common.dto.request.UserRequest;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.UserDataAccessService;
import com.preschool.identityservice.infra.entity.RoleEntity;
import com.preschool.identityservice.infra.entity.UserEntity;
import com.preschool.identityservice.infra.mapper.UserMapper;
import com.preschool.identityservice.infra.repository.RoleRepository;
import com.preschool.identityservice.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserDataAccessService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserData createUser(UserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        
        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with username " + request.getUsername() + " already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }
        
        UserEntity userEntity = userMapper.toEntity(request);
        
        // Assign roles if provided
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            List<RoleEntity> roles = roleRepository.findAllById(request.getRoleIds());
            userEntity.getRoles().addAll(roles);
        }
        
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        
        return userMapper.toData(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserData getUserById(UUID userId) {
        log.info("Getting user by ID: {}", userId);
        UserEntity user = userRepository.findByIdWithRolesAndPermissions(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return userMapper.toData(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserData getUserByKeycloakId(String keycloakId) {
        log.info("Getting user by Keycloak ID: {}", keycloakId);
        UserEntity user = userRepository.findByKeycloakIdWithRoles(keycloakId)
            .orElseThrow(() -> new RuntimeException("User not found with Keycloak ID: " + keycloakId));
        return userMapper.toData(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserData getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return userMapper.toData(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserData> getAllUsers() {
        log.info("Getting all users");
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toDataList(users);
    }

    @Override
    public UserData updateUser(UUID userId, UserRequest request) {
        log.info("Updating user with ID: {}", userId);
        
        UserEntity existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Check for unique constraints
        if (!existingUser.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with username " + request.getUsername() + " already exists");
        }
        
        if (!existingUser.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }
        
        userMapper.updateEntityFromRequest(request, existingUser);
        
        // Update roles if provided
        if (request.getRoleIds() != null) {
            existingUser.getRoles().clear();
            if (!request.getRoleIds().isEmpty()) {
                List<RoleEntity> roles = roleRepository.findAllById(request.getRoleIds());
                existingUser.getRoles().addAll(roles);
            }
        }
        
        UserEntity updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return userMapper.toData(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    public UserData syncUserFromKeycloak(String keycloakId, String username, String email) {
        log.info("Syncing user from Keycloak: {}", username);
        
        UserEntity user = userRepository.findByKeycloakId(keycloakId)
            .orElseGet(() -> {
                UserEntity newUser = new UserEntity();
                newUser.setKeycloakId(keycloakId);
                newUser.setCreatedAt(LocalDateTime.now());
                return newUser;
            });
        
        user.setUsername(username);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        
        UserEntity savedUser = userRepository.save(user);
        log.info("User synced successfully: {}", username);
        
        return userMapper.toData(savedUser);
    }

    @Override
    public void assignRoleToUser(UUID userId, UUID roleId) {
        log.info("Assigning role {} to user {}", roleId, userId);
        
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        RoleEntity role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        user.getRoles().add(role);
        userRepository.save(user);
        
        log.info("Role assigned successfully");
    }

    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        log.info("Removing role {} from user {}", roleId, userId);
        
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.getRoles().removeIf(role -> role.getRoleId().equals(roleId));
        userRepository.save(user);
        
        log.info("Role removed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserData> getUsersByRole(UUID roleId) {
        log.info("Getting users by role ID: {}", roleId);
        
        // This can be optimized with a custom query if needed
        List<UserEntity> users = userRepository.findAll();
        List<UserEntity> filteredUsers = users.stream()
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getRoleId().equals(roleId)))
            .toList();
        
        return userMapper.toDataList(filteredUsers);
    }
}
