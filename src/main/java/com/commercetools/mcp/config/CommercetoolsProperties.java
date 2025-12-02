package com.commercetools.mcp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for commercetools API client.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "commercetools")
public class CommercetoolsProperties {
    
    private String projectKey;
    private String clientId;
    private String clientSecret;
    private String authUrl;
    private String apiUrl;
    private String scopes;
}
