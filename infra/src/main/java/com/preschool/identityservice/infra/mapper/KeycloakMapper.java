package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import java.util.List;
import java.util.stream.Collectors;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Mapper interface for converting Keycloak representations to DTOs */
@Mapper(componentModel = "spring")
public interface KeycloakMapper {

    /** Maps UserRepresentation to UserData */
    @Mapping(
            target = "createdTimestamp",
            expression =
                    "java(user.getCreatedTimestamp() != null ? java.time.Instant.ofEpochMilli(user.getCreatedTimestamp()) : java.time.Instant.now())")
    UserData toUserData(UserRepresentation user, List<RoleData> roles, List<GroupData> groups);

    /** Maps GroupRepresentation to GroupData */
    GroupData toGroupData(GroupRepresentation group);

    /** Maps RoleRepresentation to RoleData */
    RoleData toRoleData(RoleRepresentation role);

    /** Maps list of RoleRepresentation to list of RoleData */
    List<RoleData> toRoleDataList(List<RoleRepresentation> roles);

    /** Maps list of GroupRepresentation to list of GroupData */
    List<GroupData> toGroupDataList(List<GroupRepresentation> groups);

    /** Maps list of group names to list of GroupData */
    default List<GroupData> map(List<String> groupNames) {
        if (groupNames == null) {
            return List.of();
        }
        return groupNames.stream()
                .map(
                        name -> {
                            GroupData groupData = new GroupData();
                            groupData.setName(name);
                            // Note: ID may not be available from group names alone
                            groupData.setId(name); // Giả định ID bằng name nếu không có GroupRepresentation
                            return groupData;
                        })
                .collect(Collectors.toList());
    }
}
