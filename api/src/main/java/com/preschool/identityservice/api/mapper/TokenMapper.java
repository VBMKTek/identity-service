package com.preschool.identityservice.api.mapper;

import com.preschool.identityservice.api.dto.response.TokenResponse;
import com.preschool.identityservice.api.dto.response.TokenValidationResponse;
import com.preschool.identityservice.core.data.JwtVerificationData;
import com.preschool.identityservice.core.data.TokenData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting Token data to response DTOs
 */
@Mapper(componentModel = "spring")
public interface TokenMapper {

    /**
     * Converts TokenData to TokenResponse
     */
    @Mapping(target = "expiresIn", expression = "java(calculateExpiresIn(tokenData.getExpiresAt()))")
    @Mapping(target = "issuedAt", expression = "java(java.time.Instant.now())")
    TokenResponse dataToResponse(TokenData tokenData);

    /**
     * Converts JwtVerificationData to TokenValidationResponse
     */
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    TokenValidationResponse verificationDataToResponse(JwtVerificationData verificationData);

    /**
     * Calculate expires in seconds
     */
    default long calculateExpiresIn(java.time.Instant expiresAt) {
        if (expiresAt == null) {
            return 0;
        }
        return java.time.Duration.between(java.time.Instant.now(), expiresAt).getSeconds();
    }
}
