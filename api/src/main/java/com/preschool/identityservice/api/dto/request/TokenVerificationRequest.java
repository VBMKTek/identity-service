package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request DTO for JWT token verification */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVerificationRequest {

    @NotBlank(message = "Token is required")
    private String token;
}
