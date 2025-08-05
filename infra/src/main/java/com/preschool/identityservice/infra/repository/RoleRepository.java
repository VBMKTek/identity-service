package com.preschool.identityservice.infra.repository;

import com.preschool.identityservice.infra.entity.RoleEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.permissions WHERE r.roleId = :roleId")
    Optional<RoleEntity> findByIdWithPermissions(@Param("roleId") UUID roleId);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.permissions WHERE r.roleName = :roleName")
    Optional<RoleEntity> findByRoleNameWithPermissions(@Param("roleName") String roleName);
}
