package com.commercetools.mcp.service;

import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for all MCP tools.
 * Auto-discovers and registers all Tool beans.
 */
@Slf4j
@Service
public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry(List<Tool> toolList) {
        toolList.forEach(tool -> {
            ToolDefinition def = tool.getDefinition();
            tools.put(def.getName(), tool);
            log.info("Registered tool: {}", def.getName());
        });
        log.info("Total tools registered: {}", tools.size());
    }

    public List<ToolDefinition> getAllToolDefinitions() {
        return tools.values().stream()
                .map(Tool::getDefinition)
                .toList();
    }

    public ToolResult executeTool(String toolName, Map<String, Object> args) {
        Tool tool = tools.get(toolName);
        if (tool == null) {
            return ToolResult.error("Unknown tool: " + toolName);
        }
        
        log.info("Executing tool: {} with args: {}", toolName, args);
        return tool.execute(args);
    }

    public boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }

    public int getToolCount() {
        return tools.size();
    }
}
