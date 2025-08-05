package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.common.dto.request.UserRequest;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.infra.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(UserRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    UserEntity toEntity(UserData userData);

    @Mapping(target = "id", source = "keycloakId")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(
            target = "createdTimestamp",
            expression =
                    "java(entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : null)")
    @Mapping(target = "groups", ignore = true)
    UserData toData(UserEntity entity);

    // New methods for clean architecture
    @Mapping(target = "id", source = "keycloakId")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(
            target = "createdTimestamp",
            expression =
                    "java(entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : null)")
    @Mapping(target = "groups", ignore = true)
    UserData toUserData(UserEntity entity);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    UserEntity toUserEntity(UserData userData);

    List<UserData> toDataList(List<UserEntity> entities);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget UserEntity entity);
}
