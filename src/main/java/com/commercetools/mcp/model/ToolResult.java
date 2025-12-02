package com.commercetools.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the result of a tool execution.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResult {
    private String content;
    private boolean isError;
    
    public static ToolResult success(String content) {
        return ToolResult.builder()
                .content(content)
                .isError(false)
                .build();
    }
    
    public static ToolResult error(String message) {
        return ToolResult.builder()
                .content(message)
                .isError(true)
                .build();
    }
}
