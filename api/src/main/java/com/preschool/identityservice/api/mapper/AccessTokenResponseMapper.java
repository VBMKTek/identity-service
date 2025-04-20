package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.response.AccessToken;
import com.preschool.identityservice.core.data.AccessTokenData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessTokenResponseMapper {
    AccessToken convertFromData(AccessTokenData accessTokenData);
}
