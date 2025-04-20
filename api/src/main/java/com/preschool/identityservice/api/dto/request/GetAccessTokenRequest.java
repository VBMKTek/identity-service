package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GetAccessTokenRequest(@NotBlank String username, @NotBlank String password) {}
