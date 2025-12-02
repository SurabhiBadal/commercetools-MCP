package com.commercetools.mcp.tools.base;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.mcp.model.ToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for all commercetools tools.
 * Provides common functionality like JSON serialization and error handling.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommercetoolsTool implements Tool {
    
    protected final ProjectApiRoot apiRoot;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Convert an object to JSON string.
     */
    protected String toJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            return "{\"error\": \"Failed to serialize response\"}";
        }
    }
    
    /**
     * Handle exceptions and return error result.
     */
    protected ToolResult handleError(String operation, Exception e) {
        log.error("Error executing {}: {}", operation, e.getMessage(), e);
        String errorMessage = String.format("Error executing %s: %s", operation, e.getMessage());
        return ToolResult.error(errorMessage);
    }
}
