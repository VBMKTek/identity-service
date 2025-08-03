package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.UserDataAccessService;
import com.preschool.identityservice.infra.entity.UserEntity;
import com.preschool.identityservice.infra.mapper.UserMapper;
import com.preschool.identityservice.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of UserDataAccessService using JPA repositories
 * This class handles the actual database operations
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDataAccessServiceImpl implements UserDataAccessService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserData> findByKeycloakId(String keycloakId) {
        log.debug("Finding user by Keycloak ID: {}", keycloakId);
        return userRepository.findByKeycloakId(keycloakId)
                .map(userMapper::toUserData);
    }

    @Override
    public Optional<UserData> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toUserData);
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(userMapper::toUserData);
    }

    @Override
    public Optional<UserData> findById(UUID userId) {
        log.debug("Finding user by ID: {}", userId);
        return userRepository.findById(userId)
                .map(userMapper::toUserData);
    }

    @Override
    public Optional<UserData> findByIdWithRolesAndPermissions(UUID userId) {
        log.debug("Finding user by ID with roles and permissions: {}", userId);
        return userRepository.findByIdWithRolesAndGroups(userId)
                .map(userMapper::toUserData);
    }

    @Override
    public List<UserData> findAll() {
        log.debug("Finding all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserData)
                .collect(Collectors.toList());
    }

    @Override
    public UserData save(UserData userData) {
        log.debug("Saving user: {}", userData.getUsername());
        UserEntity entity = userMapper.toUserEntity(userData);
        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toUserData(savedEntity);
    }

    @Override
    public void deleteById(UUID userId) {
        log.debug("Deleting user by ID: {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if user exists by username: {}", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByKeycloakId(String keycloakId) {
        log.debug("Checking if user exists by Keycloak ID: {}", keycloakId);
        return userRepository.existsByKeycloakId(keycloakId);
    }

    @Override
    public boolean existsById(UUID userId) {
        log.debug("Checking if user exists by ID: {}", userId);
        return userRepository.existsById(userId);
    }

    @Override
    public List<UserData> findByRoleId(UUID roleId) {
        log.debug("Finding users by role ID: {}", roleId);
        return userRepository.findByRolesRoleId(roleId).stream()
                .map(userMapper::toUserData)
                .collect(Collectors.toList());
    }
}
