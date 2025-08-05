package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.common.param.PermissionParam;
import com.preschool.identityservice.core.data.PermissionData;
import com.preschool.identityservice.infra.entity.PermissionEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PermissionEntity toEntity(PermissionParam request);

    @Mapping(target = "roles", ignore = true)
    PermissionData toData(PermissionEntity entity);

    List<PermissionData> toDataList(List<PermissionEntity> entities);

    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(PermissionParam request, @MappingTarget PermissionEntity entity);
}
