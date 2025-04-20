package com.preschool.identityservice.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String realm;
    private String authServerUrl;
    private String sslRequired;
    private String resource;
    private String credentialsSecret;
}
