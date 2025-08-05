package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.common.param.UserParam;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.infra.entity.UserEntity;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "groups", ignore = true)
    UserEntity toEntity(UserParam request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "groups", ignore = true)
    UserEntity toEntityFromData(UserData userData);

    @IterableMapping(qualifiedByName = "entityToData")
    List<UserData> toDataList(List<UserEntity> entities);

    @Mapping(target = "id", source = "keycloakId")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(
            target = "createdTimestamp",
            expression =
                    "java(entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : null)")
    @Mapping(target = "groups", ignore = true)
    @Named("entityToData")
    UserData toData(UserEntity entity);

    // Alias for backward compatibility
    default UserData toUserData(UserEntity entity) {
        return toData(entity);
    }

    // Convert UserData to Entity
    default UserEntity toUserEntity(UserData userData) {
        return toEntityFromData(userData);
    }

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "groups", ignore = true)
    void updateEntityFromRequest(UserParam request, @MappingTarget UserEntity entity);
}
