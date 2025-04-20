package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.request.CreateGroupRequest;
import com.preschool.identityservice.api.dto.response.GroupResponse;
import com.preschool.identityservice.api.dto.response.OperationResponse;
import com.preschool.identityservice.api.dto.response.RoleResponse;
import com.preschool.identityservice.api.dto.response.UserResponse;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.OperationData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import java.util.List;
import org.mapstruct.Mapper;

/** Mapper interface for converting DTOs to API responses */
@Mapper(componentModel = "spring")
public interface ApiKeycloakMapper {

    /** Maps CreateGroupRequest to group name */
    default String toCreateGroupParam(CreateGroupRequest request) {
        return request.getGroupName();
    }

    /** Maps UserData to UserResponse */
    UserResponse toUserResponse(UserData user);

    /** Maps RoleData to RoleResponse */
    RoleResponse toRoleResponse(RoleData role);

    /** Maps GroupData to GroupResponse */
    GroupResponse toGroupResponse(GroupData group);

    /** Maps OperationData to OperationResponse */
    OperationResponse toOperationResponse(OperationData operation);

    /** Maps list of GroupData to list of GroupResponse */
    List<GroupResponse> toGroupResponseList(List<GroupData> groups);
}
