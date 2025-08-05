package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.UserRepositoryService;
import com.preschool.identityservice.infra.mapper.UserMapper;
import com.preschool.identityservice.infra.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of UserRepositoryService for database operations */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserData> findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId).map(userMapper::toData);
    }

    @Override
    public Optional<UserData> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toData);
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toData);
    }

    @Override
    public Optional<UserData> findById(UUID userId) {
        return userRepository.findById(userId).map(userMapper::toData);
    }

    @Override
    public List<UserData> findAll() {
        return userRepository.findAll().stream().map(userMapper::toData).toList();
    }

    @Override
    public UserData save(UserData userData) {
        var entity = userMapper.toEntity(userData);
        var savedEntity = userRepository.save(entity);
        return userMapper.toData(savedEntity);
    }

    @Override
    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    @Override
    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Optional<UserData> findByIdWithRolesAndPermissions(UUID userId) {
        return userRepository.findByIdWithRolesAndPermissions(userId).map(userMapper::toData);
    }

    @Override
    public List<UserData> findByRoleId(UUID roleId) {
        // This would require a custom query or method in the repository
        // For now, we'll implement a simple version
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(roleId)))
                .map(userMapper::toData)
                .toList();
    }
}
