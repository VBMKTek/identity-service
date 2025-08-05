package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.common.param.GroupParam;
import com.preschool.identityservice.core.data.GroupData;
import com.preschool.identityservice.core.data.UserData;
import java.util.List;
import java.util.UUID;

/**
 * Interface for Group data access operations This provides abstraction for data access layer
 * following clean architecture
 */
public interface GroupDataAccessService {

    GroupData createGroup(GroupParam param);

    GroupData getGroupById(UUID groupId);

    GroupData getGroupByName(String groupName);

    List<GroupData> getAllGroups();

    GroupData updateGroup(UUID groupId, GroupParam param);

    void deleteGroup(UUID groupId);

    void addUserToGroup(UUID userId, UUID groupId);

    void removeUserFromGroup(UUID userId, UUID groupId);

    void assignRoleToGroup(UUID groupId, UUID roleId);

    void removeRoleFromGroup(UUID groupId, UUID roleId);

    List<UserData> getUsersByGroup(UUID groupId);

    List<GroupData> getGroupsByUser(UUID userId);
}
