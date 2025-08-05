package com.preschool.identityservice.api.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** JWT public key response for other services to verify tokens */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtPublicKeyResponse {

    private String algorithm;
    private String keyType;
    private String use;
    private String keyId;
    private String publicKey;
    private Map<String, Object> additionalProperties;
}
