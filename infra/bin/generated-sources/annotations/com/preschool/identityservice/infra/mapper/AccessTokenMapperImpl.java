package com.preschool.identityservice.infra.mapper;

import com.preschool.identityservice.core.data.AccessTokenData;
import javax.annotation.processing.Generated;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T21:32:06+0700",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class AccessTokenMapperImpl implements AccessTokenMapper {

    @Override
    public AccessTokenData convertFromResponse(AccessTokenResponse accessTokenResponse) {
        if ( accessTokenResponse == null ) {
            return null;
        }

        String token = null;
        long expiresIn = 0L;
        long refreshExpiresIn = 0L;
        String refreshToken = null;
        String tokenType = null;
        String idToken = null;
        String scope = null;

        token = accessTokenResponse.getToken();
        expiresIn = accessTokenResponse.getExpiresIn();
        refreshExpiresIn = accessTokenResponse.getRefreshExpiresIn();
        refreshToken = accessTokenResponse.getRefreshToken();
        tokenType = accessTokenResponse.getTokenType();
        idToken = accessTokenResponse.getIdToken();
        scope = accessTokenResponse.getScope();

        AccessTokenData accessTokenData = new AccessTokenData( token, expiresIn, refreshExpiresIn, refreshToken, tokenType, idToken, scope );

        return accessTokenData;
    }
}
