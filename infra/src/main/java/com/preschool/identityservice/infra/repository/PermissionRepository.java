package com.preschool.identityservice.infra.repository;

import com.preschool.identityservice.infra.entity.PermissionEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {

    Optional<PermissionEntity> findByPermissionName(String permissionName);

    boolean existsByPermissionName(String permissionName);
}
