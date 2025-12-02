# commercetools MCP Server - Java Spring Boot 3.3.3

A Java Spring Boot implementation of the MCP (Model Context Protocol) server for commercetools API integration.

## Overview

This project provides a Java-based MCP server that enables AI assistants (like Claude) to interact with the commercetools platform. It implements 26 tools covering customers, products, orders, payments, and more.

## Technology Stack

- **Spring Boot** 3.3.3
- **Spring AI MCP** 1.0.0-M6 (Milestone release)
- **commercetools Java SDK** v19.6.2
- **Java** 17+
- **Lombok** for reducing boilerplate
- **Jackson** for JSON processing

> **Note**: Spring AI MCP is currently in milestone release (M6). The API may change before the stable 1.0.0 release.



## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **commercetools Account** with API credentials

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/SurabhiBadal/commercetools-MCP.git
cd commercetools-MCP
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Configure Environment Variables

Create a `.env` file in the project root or set environment variables:

```bash
export CTP_PROJECT_KEY=your-project-key
export CTP_CLIENT_ID=your-client-id
export CTP_CLIENT_SECRET=your-client-secret
export CTP_AUTH_URL=https://auth.us-central1.gcp.commercetools.com
export CTP_API_URL=https://api.us-central1.gcp.commercetools.com
```

> **Note**: Update the region URLs (`us-central1.gcp`) to match your commercetools project region.

### 4. Run the Server

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/mcp-server-2.0.0.jar
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


## Integration with Claude Desktop

Configure Claude Desktop to use this MCP server by editing your Claude Desktop configuration file:

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`  
**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

Add the following configuration:

```json
{
  "mcpServers": {
    "commercetools": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/mcp-essentials/target/mcp-server-2.0.0.jar"
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

> **Important**: Replace `/absolute/path/to/mcp-essentials` with the actual path to your project directory.

After updating the configuration:
1. Restart Claude Desktop
2. The commercetools tools should appear in Claude's available tools
3. You can now ask Claude to interact with your commercetools project

## Quick Start Example

Once configured, you can ask Claude to:

```
"Can you search for products in my commercetools project?"
"Create a new customer with email test@example.com"
"Show me the details of order ID xyz-123"
"Update product ABC to change its name"
```

## Next Steps

### 1. Testing
- Write unit tests for each tool
- Set up integration tests with a commercetools sandbox project
- Test all 26 tools end-to-end with Claude Desktop

### 2. Documentation
- Add detailed API documentation for each tool
- Create usage examples and best practices guide
- Document common troubleshooting scenarios

### 3. Deployment
- Create Docker image for containerized deployment
- Set up CI/CD pipeline
- Add health checks and monitoring

### 4. Enhancements
- Add caching for frequently accessed data
- Implement rate limiting and retry logic
- Add support for batch operations

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.3.3/reference/html/)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring AI MCP GitHub](https://github.com/spring-projects/spring-ai)
- [commercetools Java SDK Documentation](https://docs.commercetools.com/sdk/jvm-sdk)
- [commercetools API Reference](https://docs.commercetools.com/api/)
- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [Claude Desktop Documentation](https://claude.ai/desktop)

## Troubleshooting

### Build Issues

If you encounter Lombok-related build errors:
```bash
mvn clean install -U
```

### Connection Issues

Verify your commercetools credentials and region:
- Check that `CTP_PROJECT_KEY`, `CTP_CLIENT_ID`, and `CTP_CLIENT_SECRET` are correct
- Ensure the `CTP_AUTH_URL` and `CTP_API_URL` match your project's region

### Claude Desktop Integration

If tools don't appear in Claude Desktop:
1. Verify the JAR path in the configuration is absolute
2. Check that environment variables are set correctly
3. Restart Claude Desktop completely
4. Check Claude Desktop logs for errors

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT

## Support

For issues and questions:
- Open an issue on [GitHub](https://github.com/SurabhiBadal/commercetools-MCP/issues)
- Check the [commercetools documentation](https://docs.commercetools.com/)
- Review the [Spring AI MCP documentation](https://docs.spring.io/spring-ai/reference/)

---

**Built with ❤️ using Spring Boot 3.3.3 and commercetools Java SDK**
