package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.common.param.RoleParam;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.infra.entity.RoleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {PermissionMapper.class})
public interface RoleMapper {

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoleEntity toEntity(RoleParam request);

    @Mapping(target = "id", source = "roleId")
    @Mapping(target = "name", source = "roleName")
    @Mapping(target = "users", ignore = true)
    RoleData toData(RoleEntity entity);

    List<RoleData> toDataList(List<RoleEntity> entities);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RoleParam request, @MappingTarget RoleEntity entity);
}
