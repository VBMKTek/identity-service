package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.request.UserRequest;
import com.preschool.identityservice.common.param.UserParam;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting UserRequest to UserParam
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts UserRequest to UserParam
     * @param request the UserRequest to convert
     * @return UserParam
     */
    UserParam requestToParam(UserRequest request);
}
