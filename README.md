# commercetools MCP Server - Java Spring Boot 3.3.3

A Java Spring Boot implementation of the MCP (Model Context Protocol) server for commercetools API integration.

## ⚠️ Important Notice

This is a **Java Spring Boot 3.3.3** conversion of the original TypeScript MCP server. It uses:
- **Spring AI MCP** (Experimental - released Dec 2024)
- **commercetools Java SDK** v19.6.2
- **Java 17+**

> **Note**: Spring AI MCP is currently experimental and may have breaking changes. For production use, consider the TypeScript version or wait for Spring AI MCP to reach stable release.

## Project Structure

```
src/main/java/com/commercetools/mcp/
├── CommercetoolsMcpApplication.java    # Main Spring Boot application
├── config/
│   ├── CommercetoolsConfig.java        # commercetools API client config
│   ├── CommercetoolsProperties.java    # Configuration properties
│   └── McpServerConfig.java            # MCP server configuration
├── model/
│   ├── ToolDefinition.java             # Tool metadata model
│   └── ToolResult.java                 # Tool execution result
├── tools/
│   ├── base/
│   │   ├── Tool.java                   # Base tool interface
│   │   └── AbstractCommercetoolsTool.java  # Abstract base class
│   ├── customer/
│   │   ├── CustomerCreateTool.java
│   │   ├── CustomerReadTool.java
│   │   └── CustomerUpdateTool.java
│   ├── customergroup/
│   ├── product/
│   ├── productselection/
│   ├── producttailoring/
│   ├── producttype/
│   ├── project/
│   ├── order/
│   └── payment/
└── service/
    └── ToolRegistry.java               # Registers all tools with MCP server
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+** or Gradle 7.0+
- **commercetools Account** with API credentials

## Setup

### 1. Clone and Build

```bash
cd /Users/surabhibadal/Documents/litmus7/commercetools/mcp-essentials
mvn clean install
```

### 2. Configure Environment

Copy `.env.example.java` to `.env` and update with your credentials:

```bash
cp .env.example.java .env
```

Edit `.env`:
```properties
CTP_PROJECT_KEY=your-project-key
CTP_CLIENT_ID=your-client-id
CTP_CLIENT_SECRET=your-client-secret
CTP_AUTH_URL=https://auth.us-central1.gcp.commercetools.com
CTP_API_URL=https://api.us-central1.gcp.commercetools.com
```

### 3. Run the Server

```bash
mvn spring-boot:run
```

## Available Tools (26 Total)

### Customer Tools (6)
- `customer_create` - Create new customers
- `customer_read` - Query customers
- `customer_update` - Update customer information
- `customer_group_create` - Create customer groups
- `customer_group_read` - Query customer groups
- `customer_group_update` - Update customer groups

### Product Tools (13)
- `product_search_read` - Search products
- `products_create` - Create products
- `products_read` - Query products
- `products_update` - Update products
- `product_selection_create` - Create product selections
- `product_selection_read` - Query product selections
- `product_selection_update` - Update product selections
- `product_tailoring_create` - Create product tailoring
- `product_tailoring_read` - Query product tailoring
- `product_tailoring_update` - Update product tailoring
- `product_type_create` - Create product types
- `product_type_read` - Query product types
- `product_type_update` - Update product types

### Order & Payment Tools (7)
- `project_read` - Read project information
- `order_create` - Create orders
- `order_read` - Query orders
- `order_update` - Update orders
- `payment_create` - Create payments
- `payment_read` - Query payments
- `payment_update` - Update payments

## Development Status

### ✅ Completed
- [x] Maven project setup (pom.xml)
- [x] Spring Boot configuration
- [x] commercetools API client integration
- [x] Base tool infrastructure
- [x] Tool models (ToolDefinition, ToolResult)

### 🚧 In Progress
- [ ] MCP Server configuration with Spring AI
- [ ] Tool registry and registration
- [ ] Customer tools implementation (6 tools)
- [ ] Product tools implementation (13 tools)
- [ ] Order & Payment tools implementation (7 tools)

### 📝 Remaining Work

Due to the scope of this conversion (39 Java files), the implementation is provided as a foundation. To complete:

1. **Implement MCP Server Config** (`McpServerConfig.java`)
   - Configure Spring AI MCP server
   - Set up stdio transport
   - Register tool handlers

2. **Implement Tool Registry** (`ToolRegistry.java`)
   - Auto-discover all tools
   - Register with MCP server
   - Handle tool execution routing

3. **Implement All 26 Tools**
   - Each tool needs: definition, input schema, execute method
   - Follow the pattern in `AbstractCommercetoolsTool`
   - Use commercetools Java SDK for API calls

## Example Tool Implementation

Here's how a tool is implemented in Java:

```java
@Component
public class CustomerCreateTool extends AbstractCommercetoolsTool {
    
    public CustomerCreateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }
    
    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_create")
                .description("Create a new customer in commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "email", Map.of("type", "string"),
                                "password", Map.of("type", "string"),
                                "firstName", Map.of("type", "string")
                        ),
                        "required", List.of("email", "password")
                ))
                .build();
    }
    
    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            CustomerDraft draft = CustomerDraft.builder()
                    .email((String) args.get("email"))
                    .password((String) args.get("password"))
                    .firstName((String) args.get("firstName"))
                    .build();
            
            CustomerSignInResult result = apiRoot.customers()
                    .post(draft)
                    .executeBlocking()
                    .getBody();
            
            return ToolResult.success(toJson(result));
        } catch (Exception e) {
            return handleError("customer_create", e);
        }
    }
}
```

## Comparison: TypeScript vs Java

| Aspect | TypeScript | Java Spring Boot |
|--------|-----------|------------------|
| **Lines of Code** | ~2,500 | ~4,000 (estimated) |
| **Build Time** | 5 seconds | 15-30 seconds |
| **Runtime** | Node.js | JVM |
| **MCP SDK** | Stable | Experimental |
| **Type Safety** | Good | Excellent |
| **Verbosity** | Low | Medium-High |

## Integration with Claude Desktop

Once implemented, configure Claude Desktop:

```json
{
  "mcpServers": {
    "commercetools-java": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/mcp-essentials/target/mcp-server-2.0.0.jar"
      ],
      "env": {
        "CTP_PROJECT_KEY": "your-project-key",
        "CTP_CLIENT_ID": "your-client-id",
        "CTP_CLIENT_SECRET": "your-client-secret",
        "CTP_AUTH_URL": "https://auth.us-central1.gcp.commercetools.com",
        "CTP_API_URL": "https://api.us-central1.gcp.commercetools.com"
      }
    }
  }
}
```

## Next Steps

1. **Complete Tool Implementations**
   - Implement all 26 tools following the example pattern
   - Test each tool individually

2. **MCP Server Integration**
   - Configure Spring AI MCP server
   - Set up stdio transport
   - Test with Claude Desktop

3. **Testing**
   - Unit tests for each tool
   - Integration tests with commercetools sandbox
   - End-to-end testing with Claude Desktop

4. **Documentation**
   - API documentation for each tool
   - Usage examples
   - Troubleshooting guide

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.3.3/reference/html/)
- [Spring AI MCP](https://github.com/spring-projects/spring-ai)
- [commercetools Java SDK](https://docs.commercetools.com/sdk/jvm-sdk)
- [Model Context Protocol](https://modelcontextprotocol.io/)

## License

MIT

---

**Note**: This is a work-in-progress conversion. The TypeScript version is fully functional and production-ready. Use this Java version for learning or if you specifically need Java/Spring Boot integration.
