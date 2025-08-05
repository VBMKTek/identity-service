package com.preschool.identityservice.infra.repository;

import com.preschool.identityservice.infra.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByKeycloakId(String keycloakId);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);

    @Query(
            "SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.userId = :userId")
    Optional<UserEntity> findByIdWithRolesAndPermissions(@Param("userId") UUID userId);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.groups WHERE u.userId = :userId")
    Optional<UserEntity> findByIdWithRolesAndGroups(@Param("userId") UUID userId);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.keycloakId = :keycloakId")
    Optional<UserEntity> findByKeycloakIdWithRoles(@Param("keycloakId") String keycloakId);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.roleId = :roleId")
    List<UserEntity> findByRolesRoleId(@Param("roleId") UUID roleId);
}
