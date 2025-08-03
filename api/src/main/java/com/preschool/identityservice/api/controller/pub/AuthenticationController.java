package com.preschool.identityservice.api.controller.pub;

import com.preschool.identityservice.api.dto.request.GetAccessTokenRequest;
import com.preschool.identityservice.api.dto.response.AccessToken;
import com.preschool.identityservice.api.mapper.AccessTokenResponseMapper;
import com.preschool.identityservice.core.service.AuthenticationService;
import com.preschool.identityservice.core.service.infra.LogoutService;
import com.preschool.libraries.base.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccessTokenResponseMapper accessTokenResponseMapper;
    private final LogoutService logoutService;

    @PostMapping("/login")
    @SneakyThrows
    public ResponseEntity<Response<AccessToken>> getAccessToken(
            @RequestBody @Valid GetAccessTokenRequest getAccessTokenRequest) {
        return authenticationService
                .getAccessToken(getAccessTokenRequest.username(), getAccessTokenRequest.password())
                .map(accessTokenResponseMapper::convertFromData)
                .map(Response::success)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @PreAuthorize("hasRole('default-roles-preschool_be_sit')")
    @PostMapping("/logout")
    public ResponseEntity<Response<String>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        
        // Get user ID from JWT token
        String userId = jwtAuthenticationToken.getToken().getClaimAsString("sub");
        
        // Revoke all tokens and sessions for the user
        boolean logoutSuccess = logoutService.logout(userId);
        
        if (logoutSuccess) {
            return ResponseEntity.ok(Response.success("Logged out successfully"));
        } else {
            throw new RuntimeException("Logout failed");
        }
    }
}
