package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.response.GroupResponse;
import com.preschool.identityservice.api.dto.response.OperationResponse;
import com.preschool.identityservice.api.dto.response.RoleResponse;
import com.preschool.identityservice.api.dto.response.UserResponse;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.OperationData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T21:32:04+0700",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ApiKeycloakMapperImpl implements ApiKeycloakMapper {

    @Override
    public UserResponse toUserResponse(UserData user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setCreatedTimestamp( user.getCreatedTimestamp() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setEnabled( user.isEnabled() );
        userResponse.setGroups( toGroupResponseList( user.getGroups() ) );
        userResponse.setId( user.getId() );
        userResponse.setRoles( roleDataListToRoleResponseList( user.getRoles() ) );
        userResponse.setUsername( user.getUsername() );

        return userResponse;
    }

    @Override
    public RoleResponse toRoleResponse(RoleData role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse roleResponse = new RoleResponse();

        roleResponse.setId( role.getId() );
        roleResponse.setName( role.getName() );

        return roleResponse;
    }

    @Override
    public GroupResponse toGroupResponse(GroupData group) {
        if ( group == null ) {
            return null;
        }

        GroupResponse groupResponse = new GroupResponse();

        groupResponse.setId( group.getId() );
        groupResponse.setName( group.getName() );

        return groupResponse;
    }

    @Override
    public OperationResponse toOperationResponse(OperationData operation) {
        if ( operation == null ) {
            return null;
        }

        OperationResponse operationResponse = new OperationResponse();

        operationResponse.setGroupId( operation.getGroupId() );
        operationResponse.setMessage( operation.getMessage() );
        operationResponse.setRoleName( operation.getRoleName() );
        operationResponse.setSuccess( operation.isSuccess() );
        operationResponse.setUserId( operation.getUserId() );

        return operationResponse;
    }

    @Override
    public List<GroupResponse> toGroupResponseList(List<GroupData> groups) {
        if ( groups == null ) {
            return null;
        }

        List<GroupResponse> list = new ArrayList<GroupResponse>( groups.size() );
        for ( GroupData groupData : groups ) {
            list.add( toGroupResponse( groupData ) );
        }

        return list;
    }

    protected List<RoleResponse> roleDataListToRoleResponseList(List<RoleData> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleResponse> list1 = new ArrayList<RoleResponse>( list.size() );
        for ( RoleData roleData : list ) {
            list1.add( toRoleResponse( roleData ) );
        }

        return list1;
    }
}
