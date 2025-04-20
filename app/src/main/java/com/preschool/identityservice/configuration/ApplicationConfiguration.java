package com.preschool.identityservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.preschool.identityservice.common.properties.KeycloakProperties;
import com.preschool.libraries.base.common.AppObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final KeycloakProperties keycloakProperties;

    @Bean
    public KeycloakBuilder keycloakBuilder() {
        return KeycloakBuilder.builder()
                .realm(keycloakProperties.getRealm())
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .clientId(keycloakProperties.getResource())
                .clientSecret(keycloakProperties.getCredentialsSecret())
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(100).build());
    }

    @Bean
    public Keycloak keycloak() {
        return keycloakBuilder().grantType(OAuth2Constants.CLIENT_CREDENTIALS).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new AppObjectMapper();
    }
}
