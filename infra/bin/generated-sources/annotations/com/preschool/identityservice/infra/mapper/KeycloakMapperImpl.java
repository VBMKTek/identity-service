package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.RoleData;
import com.preschool.identityservice.core.data.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T21:32:06+0700",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class KeycloakMapperImpl implements KeycloakMapper {

    @Override
    public UserData toUserData(UserRepresentation user, List<RoleData> roles, List<GroupData> groups) {
        if ( user == null && roles == null && groups == null ) {
            return null;
        }

        UserData userData = new UserData();

        if ( user != null ) {
            userData.setEmail( user.getEmail() );
            if ( user.isEnabled() != null ) {
                userData.setEnabled( user.isEnabled() );
            }
            userData.setGroups( map( user.getGroups() ) );
            userData.setId( user.getId() );
            userData.setUsername( user.getUsername() );
        }
        List<RoleData> list1 = roles;
        if ( list1 != null ) {
            userData.setRoles( new ArrayList<RoleData>( list1 ) );
        }
        userData.setCreatedTimestamp( user.getCreatedTimestamp() != null ? java.time.Instant.ofEpochMilli(user.getCreatedTimestamp()) : java.time.Instant.now() );

        return userData;
    }

    @Override
    public GroupData toGroupData(GroupRepresentation group) {
        if ( group == null ) {
            return null;
        }

        GroupData groupData = new GroupData();

        groupData.setId( group.getId() );
        groupData.setName( group.getName() );

        return groupData;
    }

    @Override
    public RoleData toRoleData(RoleRepresentation role) {
        if ( role == null ) {
            return null;
        }

        RoleData roleData = new RoleData();

        roleData.setId( role.getId() );
        roleData.setName( role.getName() );

        return roleData;
    }

    @Override
    public List<RoleData> toRoleDataList(List<RoleRepresentation> roles) {
        if ( roles == null ) {
            return null;
        }

        List<RoleData> list = new ArrayList<RoleData>( roles.size() );
        for ( RoleRepresentation roleRepresentation : roles ) {
            list.add( toRoleData( roleRepresentation ) );
        }

        return list;
    }

    @Override
    public List<GroupData> toGroupDataList(List<GroupRepresentation> groups) {
        if ( groups == null ) {
            return null;
        }

        List<GroupData> list = new ArrayList<GroupData>( groups.size() );
        for ( GroupRepresentation groupRepresentation : groups ) {
            list.add( toGroupData( groupRepresentation ) );
        }

        return list;
    }
}
