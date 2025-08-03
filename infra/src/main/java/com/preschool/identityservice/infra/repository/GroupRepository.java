package com.preschool.identityservice.infra.repository;

import com.preschool.identityservice.infra.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {
    
    Optional<GroupEntity> findByGroupName(String groupName);
    
    Optional<GroupEntity> findByKeycloakId(String keycloakId);
    
    boolean existsByGroupName(String groupName);
    
    boolean existsByKeycloakId(String keycloakId);
    
    @Query("SELECT g FROM GroupEntity g LEFT JOIN FETCH g.users LEFT JOIN FETCH g.roles WHERE g.groupId = :groupId")
    Optional<GroupEntity> findByIdWithUsersAndRoles(@Param("groupId") UUID groupId);
    
    @Query("SELECT g FROM GroupEntity g LEFT JOIN FETCH g.roles WHERE g.groupName = :groupName")
    Optional<GroupEntity> findByGroupNameWithRoles(@Param("groupName") String groupName);
}
