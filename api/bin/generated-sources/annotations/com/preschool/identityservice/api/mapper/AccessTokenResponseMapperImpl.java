package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.response.AccessToken;
import com.preschool.identityservice.core.data.AccessTokenData;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T21:32:04+0700",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class AccessTokenResponseMapperImpl implements AccessTokenResponseMapper {

    @Override
    public AccessToken convertFromData(AccessTokenData accessTokenData) {
        if ( accessTokenData == null ) {
            return null;
        }

        String token = null;
        long expiresIn = 0L;
        long refreshExpiresIn = 0L;
        String refreshToken = null;
        String tokenType = null;
        String idToken = null;
        String scope = null;

        token = accessTokenData.token();
        expiresIn = accessTokenData.expiresIn();
        refreshExpiresIn = accessTokenData.refreshExpiresIn();
        refreshToken = accessTokenData.refreshToken();
        tokenType = accessTokenData.tokenType();
        idToken = accessTokenData.idToken();
        scope = accessTokenData.scope();

        AccessToken accessToken = new AccessToken( token, expiresIn, refreshExpiresIn, refreshToken, tokenType, idToken, scope );

        return accessToken;
    }
}
