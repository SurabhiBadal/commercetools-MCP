package com.commercetools.mcp.config;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for commercetools API client.
 * Sets up the ProjectApiRoot with client credentials authentication.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommercetoolsConfig {

    private final CommercetoolsProperties properties;

    @Bean
    public ProjectApiRoot projectApiRoot() {
        log.info("Initializing commercetools API client for project: {}", properties.getProjectKey());
        
        // Create client credentials
        ClientCredentials credentials = ClientCredentials.of()
                .withClientId(properties.getClientId())
                .withClientSecret(properties.getClientSecret())
                .withScopes(properties.getScopes())
                .build();

        // Build API root with client credentials flow
        ProjectApiRoot apiRoot = ApiRootBuilder.of()
                .defaultClient(
                        credentials,
                        ServiceRegion.GCP_US_CENTRAL1 // This will be overridden by custom URLs
                )
                .build(properties.getProjectKey());

        log.info("commercetools API client initialized successfully");
        return apiRoot;
    }
}
