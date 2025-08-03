# JWT Token Management API

## Overview
Identity Service now provides centralized JWT token management, eliminating the need for each microservice to configure Keycloak directly. Instead of configuring Keycloak as the issuer in 100+ services, they can now use Identity Service as the JWT issuer.

## Architecture Benefits

### Before (Distributed JWT Verification)
```
Service A ─── Keycloak (verify JWT)
Service B ─── Keycloak (verify JWT)  
Service C ─── Keycloak (verify JWT)
...
Service N ─── Keycloak (verify JWT)
```
**Problem**: Need to configure Keycloak in every service

### After (Centralized JWT Verification)
```
Service A ──┐
Service B ──┤
Service C ──┼── Identity Service ─── Keycloak
...         │   (JWT issuer/verifier)
Service N ──┘
```
**Solution**: Only Identity Service connects to Keycloak, other services use Identity Service

## API Endpoints

### 1. Generate Token
**POST** `/v1/public/token/generate`

Generate JWT access and refresh tokens for authenticated user.

**Request:**
```json
{
    "username": "john.doe",
    "password": "password123"
}
```

**Response:**
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
    "token_type": "Bearer",
    "expires_at": "2025-08-04T10:30:00Z",
    "refresh_expires_at": "2025-08-11T09:30:00Z",
    "user_id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "john.doe",
    "email": "john.doe@example.com",
    "roles": ["USER", "ADMIN"],
    "permissions": ["READ", "WRITE"]
}
```

### 2. Verify Token ⭐ **Most Important for Other Services**
**POST** `/v1/public/token/verify`

Verify JWT token and return user information. **This is the endpoint other services should call.**

**Request:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response:**
```json
{
    "valid": true,
    "subject": "john.doe",
    "username": "john.doe", 
    "email": "john.doe@example.com",
    "roles": ["USER", "ADMIN"],
    "permissions": ["READ", "WRITE"],
    "issued_at": "2025-08-04T09:30:00Z",
    "expires_at": "2025-08-04T10:30:00Z",
    "error": null
}
```

**Invalid Token Response:**
```json
{
    "valid": false,
    "error": "Token expired"
}
```

### 3. Refresh Token
**POST** `/v1/public/token/refresh`

Generate new access token using refresh token.

**Request:**
```json
{
    "refresh_token": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response:**
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
    "token_type": "Bearer",
    "expires_at": "2025-08-04T11:30:00Z",
    "refresh_expires_at": "2025-08-11T10:30:00Z"
}
```

### 4. Revoke Token
**POST** `/v1/public/token/revoke`

Revoke JWT token (add to blacklist).

**Request:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response:** `200 OK`

### 5. Get Public Key
**GET** `/v1/public/token/public-key`

Get public key for JWT verification (alternative for services that want to verify locally).

**Response:**
```json
{
    "public_key": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----",
    "algorithm": "RS256",
    "key_id": "key-1"
}
```

## Integration Guide for Other Services

### Option 1: Call Verification API (Recommended)
Other services should call `/v1/public/token/verify` endpoint:

```java
@RestController
public class SomeController {
    
    @Autowired
    private IdentityServiceClient identityClient;
    
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        
        // Call Identity Service to verify token
        TokenValidationResponse validation = identityClient.verifyToken(token);
        
        if (!validation.isValid()) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        // Use validation.getUsername(), validation.getRoles(), etc.
        return ResponseEntity.ok("Access granted for: " + validation.getUsername());
    }
}
```

### Option 2: Local Verification (Advanced)
Services can get public key and verify locally:

```java
@Component
public class JwtVerifier {
    
    private final RSAPublicKey publicKey;
    
    public JwtVerifier() {
        // Get public key from Identity Service once
        this.publicKey = getPublicKeyFromIdentityService();
    }
    
    public boolean verifyToken(String token) {
        // Verify JWT locally using public key
        // Implementation details...
    }
}
```

## Configuration

### Environment Variables
```yaml
application:
  jwt:
    secret: ${JWT_SECRET:myVerySecretKeyForJWTToken1234567890}
    access-token-expiration: ${JWT_ACCESS_EXPIRATION:60}    # minutes
    refresh-token-expiration: ${JWT_REFRESH_EXPIRATION:7}   # days  
    issuer: ${JWT_ISSUER:identity-service}
```

## Security Considerations

1. **Token Storage**: Store refresh tokens securely (httpOnly cookies recommended)
2. **Token Blacklist**: Revoked tokens are stored in memory (use Redis in production)
3. **Key Rotation**: Implement periodic key rotation for enhanced security
4. **HTTPS Only**: All token operations must use HTTPS in production
5. **Rate Limiting**: Implement rate limiting on token generation endpoints

## Migration Strategy

1. **Phase 1**: Deploy Identity Service with JWT APIs
2. **Phase 2**: Update client applications to use new token generation
3. **Phase 3**: Update microservices to use token verification API
4. **Phase 4**: Remove Keycloak configuration from individual services

## Example Integration

### Frontend Application
```javascript
// Login
const response = await fetch('/api/identity-service/v1/public/token/generate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: 'user', password: 'pass' })
});
const tokenData = await response.json();
localStorage.setItem('access_token', tokenData.access_token);
```

### Microservice Integration
```java
// Spring Security configuration
@Configuration
public class SecurityConfig {
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(identityServiceClient);
    }
}

// JWT Filter
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final IdentityServiceClient identityClient;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        String token = extractToken(request);
        if (token != null) {
            TokenValidationResponse validation = identityClient.verifyToken(token);
            if (validation.isValid()) {
                // Set authentication context
                setAuthentication(validation);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

This centralized approach provides better security, easier maintenance, and consistent JWT handling across all services.
