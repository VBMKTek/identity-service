package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.request.RoleRequest;
import com.preschool.identityservice.common.param.RoleParam;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting RoleRequest to RoleParam
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * Converts RoleRequest to RoleParam
     * @param request the RoleRequest to convert
     * @return RoleParam
     */
    RoleParam requestToParam(RoleRequest request);
}
