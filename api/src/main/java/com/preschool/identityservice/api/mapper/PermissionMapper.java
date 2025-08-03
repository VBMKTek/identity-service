package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.request.PermissionRequest;
import com.preschool.identityservice.common.param.PermissionParam;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting PermissionRequest to PermissionParam
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     * Converts PermissionRequest to PermissionParam
     * @param request the PermissionRequest to convert
     * @return PermissionParam
     */
    PermissionParam requestToParam(PermissionRequest request);
}
