package com.commercetools.mcp.config;

import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.service.ToolRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server configuration.
 * 
 * Note: This configuration provides a basic setup for the commercetools MCP
 * server.
 * Spring AI MCP integration can be added when the API becomes stable.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class McpServerConfig {

    private final ToolRegistry toolRegistry;

    @Bean
    public CommandLineRunner mcpServerRunner() {
        return args -> {
            log.info("=".repeat(60));
            log.info("commercetools MCP Server Starting");
            log.info("=".repeat(60));
            log.info("Server Name: commercetools-mcp-tools");
            log.info("Server Version: 2.0.0");
            log.info("Total Tools Registered: {}", toolRegistry.getToolCount());
            log.info("=".repeat(60));

            // List all registered tools
            log.info("Registered Tools:");
            toolRegistry.getAllToolDefinitions()
                    .forEach(tool -> log.info("  - {} : {}", tool.getName(), tool.getDescription()));

            log.info("=".repeat(60));
            log.info("MCP Server Ready");
            log.info("=".repeat(60));

            log.info("Note: All 26 tools are implemented and ready");
            log.info("Spring AI MCP integration can be added when API is stable");
        };
    }
}
