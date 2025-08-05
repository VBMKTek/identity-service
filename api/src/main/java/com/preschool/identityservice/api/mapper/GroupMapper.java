package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.request.GroupRequest;
import com.preschool.identityservice.common.param.GroupParam;
import org.mapstruct.Mapper;

/** MapStruct mapper for converting GroupRequest to GroupParam */
@Mapper(componentModel = "spring")
public interface GroupMapper {

    /**
     * Converts GroupRequest to GroupParam
     *
     * @param request the GroupRequest to convert
     * @return GroupParam
     */
    GroupParam requestToParam(GroupRequest request);
}
