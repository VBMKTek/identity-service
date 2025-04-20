package com.preschool.identityservice.configuration.security;

import com.preschool.identityservice.core.constants.ApplicationConstants;
import com.preschool.libraries.security.config.SecurityConfigurationAbstract;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration extends SecurityConfigurationAbstract {
    @Override
    public String[] publicUrls() {
        return ApplicationConstants.PUBLIC_URLS;
    }
}
