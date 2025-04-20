package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.core.data.AccessTokenData;
import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessTokenMapper {
    AccessTokenData convertFromResponse(AccessTokenResponse accessTokenResponse);
}
