package com.commercetools.mcp.tools.base;

import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;

import java.util.Map;

/**
 * Base interface for all MCP tools.
 */
public interface Tool {
    
    /**
     * Get the tool definition including name, description, and input schema.
     */
    ToolDefinition getDefinition();
    
    /**
     * Execute the tool with the given arguments.
     * 
     * @param args Tool arguments as a map
     * @return Tool execution result
     */
    ToolResult execute(Map<String, Object> args);
}
