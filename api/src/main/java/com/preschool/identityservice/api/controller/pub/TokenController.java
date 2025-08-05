package com.preschool.identityservice.api.controller.pub;

import com.preschool.identityservice.api.dto.request.GetAccessTokenRequest;
import com.preschool.identityservice.api.dto.request.RefreshTokenRequest;
import com.preschool.identityservice.api.dto.request.TokenVerificationRequest;
import com.preschool.identityservice.api.dto.response.TokenResponse;
import com.preschool.identityservice.api.dto.response.TokenValidationResponse;
import com.preschool.identityservice.api.mapper.TokenMapper;
import com.preschool.identityservice.core.data.JwtVerificationData;
import com.preschool.identityservice.core.data.TokenData;
import com.preschool.identityservice.core.service.JwtBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public controller for JWT token operations These endpoints are used by other services to verify
 * and manage JWT tokens
 */
@RestController
@RequestMapping("/v1/public/token")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Token Management", description = "JWT Token generation, verification and management")
public class TokenController {

    private final JwtBusinessService jwtBusinessService;
    private final TokenMapper tokenMapper;

    @Operation(
            summary = "Generate JWT Token",
            description = "Generate JWT access and refresh tokens for authenticated user")
    @PostMapping("/generate")
    public ResponseEntity<TokenResponse> generateToken(
            @Valid @RequestBody GetAccessTokenRequest request) {
        log.info("Generating JWT token for user: {}", request.username());

        TokenData tokenData = jwtBusinessService.generateToken(request.username(), request.password());
        TokenResponse response = tokenMapper.dataToResponse(tokenData);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Verify JWT Token",
            description =
                    "Verify JWT token and return user information. Used by other services to validate tokens.")
    @PostMapping("/verify")
    public ResponseEntity<TokenValidationResponse> verifyToken(
            @Valid @RequestBody TokenVerificationRequest request) {
        log.debug("Verifying JWT token");

        JwtVerificationData verificationData = jwtBusinessService.verifyToken(request.getToken());
        TokenValidationResponse response = tokenMapper.verificationDataToResponse(verificationData);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh JWT Token",
            description = "Generate new access token using refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refreshing JWT token");

        TokenData tokenData = jwtBusinessService.refreshToken(request.getRefreshToken());
        TokenResponse response = tokenMapper.dataToResponse(tokenData);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Revoke JWT Token", description = "Revoke JWT token (add to blacklist)")
    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeToken(@Valid @RequestBody TokenVerificationRequest request) {
        log.info("Revoking JWT token");

        jwtBusinessService.revokeToken(request.getToken());

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get JWT Public Key",
            description = "Get public key for JWT verification (for other services)")
    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        log.debug("Getting JWT public key");

        // TODO: Implement public key retrieval
        // This would return the public key that other services can use to verify JWT signatures

        return ResponseEntity.ok("Public key endpoint - to be implemented");
    }
}
