package com.preschool.identityservice.api.dto.response;

public record AccessToken(
        String token,
        long expiresIn,
        long refreshExpiresIn,
        String refreshToken,
        String tokenType,
        String idToken,
        String scope) {}
