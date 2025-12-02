package com.commercetools.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot application for commercetools MCP Server.
 * 
 * This application provides an MCP (Model Context Protocol) server that exposes
 * 26 tools for interacting with commercetools APIs including:
 * - Customer management (6 tools)
 * - Product management (13 tools)
 * - Order management (3 tools)
 * - Payment management (3 tools)
 * - Project information (1 tool)
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.commercetools.mcp")
public class CommercetoolsMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommercetoolsMcpApplication.class, args);
    }
}
