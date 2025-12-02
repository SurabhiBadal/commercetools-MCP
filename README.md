# commercetools MCP Tools Server

An MCP (Model Context Protocol) server that provides AI assistants like Claude Desktop with comprehensive tools to interact with commercetools customer and product data.

## Features

This MCP server exposes **26 tools** for managing customers, products, orders, payments, and project settings in commercetools:

### Customer Tools (6 tools)
- **customer_create** - Create new customers with email, password, and profile information
- **customer_read** - Query customers by ID, key, email, or custom predicates
- **customer_update** - Update customer information using commercetools update actions

### Customer Group Tools (3 tools)
- **customer_group_create** - Create new customer groups
- **customer_group_read** - Query customer groups by ID, key, or predicates
- **customer_group_update** - Update customer group properties

### Product Tools (13 tools)
- **product_search_read** - Search products with full-text search, filtering, and faceting
- **products_create** - Create new products with variants, prices, and attributes
- **products_read** - Query products by ID, key, or predicates
- **products_update** - Update product information, prices, and attributes
- **product_selection_create** - Create product selections for different sales channels
- **product_selection_read** - Query product selections
- **product_selection_update** - Update product selections and manage product assignments
- **product_tailoring_create** - Create store-specific product customizations
- **product_tailoring_read** - Query product tailoring
- **product_tailoring_update** - Update product tailoring for stores
- **product_type_create** - Create product types with attribute definitions
- **product_type_read** - Query product types
- **product_type_update** - Update product type structures and attributes

### Project Tools (1 tool)
- **project_read** - Read project information including currencies, languages, countries, and settings

### Order Tools (3 tools)
- **order_create** - Create orders from carts, quotes, or by importing
- **order_read** - Query orders by ID, order number, or predicates
- **order_update** - Update order state, payment status, shipment status

### Payment Tools (3 tools)
- **payment_create** - Create payment records with transactions
- **payment_read** - Query payments by ID, key, or predicates
- **payment_update** - Update payment transactions and status

## Prerequisites

- Node.js 18 or higher
- commercetools account with API credentials
- Claude Desktop (for testing with AI assistant)

## Installation

1. Clone or download this repository
2. Install dependencies:
   ```bash
   npm install
   ```

3. Copy `.env.example` to `.env` and configure your commercetools credentials:
   ```bash
   cp .env.example .env
   ```

4. Edit `.env` with your commercetools API credentials:
   ```env
   CTP_PROJECT_KEY=your-project-key
   CTP_CLIENT_ID=your-client-id
   CTP_CLIENT_SECRET=your-client-secret
   CTP_REGION=us-central1
   CTP_AUTH_URL=https://auth.us-central1.gcp.commercetools.com
   CTP_API_URL=https://api.us-central1.gcp.commercetools.com
   ```

5. Build the project:
   ```bash
   npm run build
   ```

## Configuration

### Getting commercetools API Credentials

1. Log in to the [commercetools Merchant Center](https://mc.commercetools.com/)
2. Navigate to Settings → Developer settings → API clients
3. Create a new API client with the following scopes:
   - `manage_customers:{projectKey}`
   - `view_customers:{projectKey}`
   - `manage_products:{projectKey}`
   - `view_products:{projectKey}`
   - `manage_orders:{projectKey}`
   - `view_orders:{projectKey}`
   - `manage_payments:{projectKey}`
   - `view_payments:{projectKey}`
4. Copy the credentials to your `.env` file

### Available Regions

Choose the appropriate region for your commercetools project:

- **GCP US**: `us-central1`
  - Auth: `https://auth.us-central1.gcp.commercetools.com`
  - API: `https://api.us-central1.gcp.commercetools.com`

- **GCP Europe**: `europe-west1`
  - Auth: `https://auth.europe-west1.gcp.commercetools.com`
  - API: `https://api.europe-west1.gcp.commercetools.com`

- **AWS US**: `us-east-2`
  - Auth: `https://auth.us-east-2.aws.commercetools.com`
  - API: `https://api.us-east-2.aws.commercetools.com`

- **AWS Europe**: `eu-central-1`
  - Auth: `https://auth.eu-central-1.aws.commercetools.com`
  - API: `https://api.eu-central-1.aws.commercetools.com`

## Claude Desktop Integration

1. Locate your Claude Desktop configuration file:
   - **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
   - **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

2. Add the MCP server configuration (see `claude_desktop_config.json` for example):
   ```json
   {
     "mcpServers": {
       "commercetools-customer-tools": {
         "command": "node",
         "args": [
           "/absolute/path/to/mcp-essentials/build/index.js"
         ],
         "env": {
           "CTP_PROJECT_KEY": "your-project-key",
           "CTP_CLIENT_ID": "your-client-id",
           "CTP_CLIENT_SECRET": "your-client-secret",
           "CTP_REGION": "us-central1",
           "CTP_AUTH_URL": "https://auth.us-central1.gcp.commercetools.com",
           "CTP_API_URL": "https://api.us-central1.gcp.commercetools.com"
         }
       }
     }
   }
   ```

3. Restart Claude Desktop

4. Verify the tools are available by asking Claude: "What tools do you have access to?"

## Tool Usage Examples

### Creating a Customer

Ask Claude:
```
Create a new customer with email "john.doe@example.com", 
first name "John", last name "Doe", and password "SecurePass123"
```

### Reading Customers

Ask Claude:
```
Find all customers with email containing "example.com"
```

Or:
```
Get customer with ID "abc-123-def"
```

### Updating a Customer

Ask Claude:
```
Update customer with ID "abc-123" (version 1) to change 
their first name to "Jane"
```

The update action format:
```json
{
  "id": "customer-id",
  "version": 1,
  "actions": [
    {
      "action": "setFirstName",
      "firstName": "Jane"
    }
  ]
}
```

### Creating a Customer Group

Ask Claude:
```
Create a customer group named "VIP Customers" with key "vip"
```

### Reading Customer Groups

Ask Claude:
```
List all customer groups
```

### Updating a Customer Group

Ask Claude:
```
Update customer group with key "vip" (version 1) to change 
the name to "Premium VIP Customers"
```

## Available Update Actions

### Customer Update Actions
- `setFirstName` - Change first name
- `setLastName` - Change last name
- `setMiddleName` - Change middle name
- `changeEmail` - Change email address
- `setDateOfBirth` - Set date of birth
- `setCompanyName` - Set company name
- `setVatId` - Set VAT ID
- `addAddress` - Add a new address
- `changeAddress` - Change an existing address
- `removeAddress` - Remove an address
- `setDefaultShippingAddress` - Set default shipping address
- `setDefaultBillingAddress` - Set default billing address
- `setCustomerGroup` - Assign to a customer group
- `setKey` - Set customer key

### Customer Group Update Actions
- `changeName` - Change group name
- `setKey` - Set group key
- `setCustomType` - Set custom type
- `setCustomField` - Set custom field value

## Error Handling

The MCP server provides detailed error messages for common issues:

- **Authentication errors**: Check your API credentials in the configuration
- **Duplicate email**: Customer with this email already exists
- **Version mismatch**: The resource was modified by another process (reload and retry)
- **Invalid parameters**: Check the tool input schema requirements
- **Permission errors**: Ensure your API client has the required scopes

## Development

### Running in Development Mode

```bash
npm run dev
```

### Watching for Changes

```bash
npm run watch
```

### Project Structure

```
mcp-essentials/
├── src/
│   ├── config/
│   │   └── client.ts          # commercetools API client configuration
│   ├── tools/
│   │   ├── customer/
│   │   │   ├── create.ts      # customer.create tool
│   │   │   ├── read.ts        # customer.read tool
│   │   │   ├── update.ts      # customer.update tool
│   │   │   └── index.ts       # Customer tools export
│   │   └── customer-group/
│   │       ├── create.ts      # customer-group.create tool
│   │       ├── read.ts        # customer-group.read tool
│   │       ├── update.ts      # customer-group.update tool
│   │       └── index.ts       # Customer group tools export
│   └── index.ts               # MCP server entry point
├── build/                     # Compiled JavaScript (generated)
├── package.json
├── tsconfig.json
├── .env.example
└── README.md
```

## Troubleshooting

### MCP Server Not Appearing in Claude Desktop

1. Check that the path in `claude_desktop_config.json` is absolute and correct
2. Ensure the project is built (`npm run build`)
3. Restart Claude Desktop completely
4. Check Claude Desktop logs for errors

### Authentication Failures

1. Verify your credentials are correct in the configuration
2. Ensure your API client has the required scopes
3. Check that the region URLs match your project's region
4. Verify the project key is correct

### Tool Execution Errors

1. Check the error message returned by the tool
2. Verify the input parameters match the schema
3. For update operations, ensure you have the correct version number
4. Check commercetools API documentation for specific requirements

## License

MIT

## Resources

- [commercetools API Documentation](https://docs.commercetools.com/api/)
- [Model Context Protocol Documentation](https://modelcontextprotocol.io/)
- [commercetools TypeScript SDK](https://docs.commercetools.com/sdk/typescript-sdk)
