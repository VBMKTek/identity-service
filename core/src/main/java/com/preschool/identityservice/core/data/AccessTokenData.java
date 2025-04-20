package com.preschool.identityservice.core.data;

public record AccessTokenData(
        String token,
        long expiresIn,
        long refreshExpiresIn,
        String refreshToken,
        String tokenType,
        String idToken,
        String scope) {}
