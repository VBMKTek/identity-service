package com.preschool.identityservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for the Identity Service
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Identity Service API")
                        .description("API documentation for the Identity Service - User, Role, Group, and Permission management")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("VBMK-Tek Team")
                                .email("support@vbmk-tek.com")
                                .url("https://vbmk-tek.com"))
                        .license(new License()
                                .name("Private License")
                                .url("https://vbmk-tek.com/license")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token for authentication"))
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("OAuth2 with Keycloak")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("oauth2"));
    }
}
