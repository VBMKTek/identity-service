package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.common.dto.request.GroupRequest;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.infra.entity.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class})
public interface GroupMapper {
    
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupEntity toEntity(GroupRequest request);
    
    @Mapping(target = "id", source = "keycloakId")
    @Mapping(target = "name", source = "groupName")
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "roles", ignore = true)
    GroupData toData(GroupEntity entity);
    
    List<GroupData> toDataList(List<GroupEntity> entities);
    
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(GroupRequest request, @MappingTarget GroupEntity entity);
}
