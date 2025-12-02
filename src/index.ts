

import { Server } from '@modelcontextprotocol/sdk/server/index.js';
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js';
import {
    CallToolRequestSchema,
    ListToolsRequestSchema,
} from '@modelcontextprotocol/sdk/types.js';


import {
    customerCreateTool,
    handleCustomerCreate,
    customerReadTool,
    handleCustomerRead,
    customerUpdateTool,
    handleCustomerUpdate,
} from './tools/customer/index.js';


import {
    customerGroupCreateTool,
    handleCustomerGroupCreate,
    customerGroupReadTool,
    handleCustomerGroupRead,
    customerGroupUpdateTool,
    handleCustomerGroupUpdate,
} from './tools/customer-group/index.js';


import {
    productSearchTool,
    handleProductSearch,
} from './tools/product-search/index.js';


import {
    productsCreateTool,
    handleProductsCreate,
    productsReadTool,
    handleProductsRead,
    productsUpdateTool,
    handleProductsUpdate,
} from './tools/products/index.js';


import {
    productSelectionCreateTool,
    handleProductSelectionCreate,
    productSelectionReadTool,
    handleProductSelectionRead,
    productSelectionUpdateTool,
    handleProductSelectionUpdate,
} from './tools/product-selection/index.js';


import {
    productTailoringCreateTool,
    handleProductTailoringCreate,
    productTailoringReadTool,
    handleProductTailoringRead,
    productTailoringUpdateTool,
    handleProductTailoringUpdate,
} from './tools/product-tailoring/index.js';


import {
    productTypeCreateTool,
    handleProductTypeCreate,
    productTypeReadTool,
    handleProductTypeRead,
    productTypeUpdateTool,
    handleProductTypeUpdate,
} from './tools/product-type/index.js';


import {
    projectReadTool,
    handleProjectRead,
} from './tools/project/index.js';


import {
    orderCreateTool,
    handleOrderCreate,
    orderReadTool,
    handleOrderRead,
    orderUpdateTool,
    handleOrderUpdate,
} from './tools/order/index.js';


import {
    paymentCreateTool,
    handlePaymentCreate,
    paymentReadTool,
    handlePaymentRead,
    paymentUpdateTool,
    handlePaymentUpdate,
} from './tools/payment/index.js';

const server = new Server(
    {
        name: 'commercetools-mcp-tools',
        version: '2.0.0',
    },
    {
        capabilities: {
            tools: {},
        },
    }
);

// Register all tools
const tools = [
    // Customer tools
    customerCreateTool,
    customerReadTool,
    customerUpdateTool,
    customerGroupCreateTool,
    customerGroupReadTool,
    customerGroupUpdateTool,
    // Product search
    productSearchTool,
    // Products tools
    productsCreateTool,
    productsReadTool,
    productsUpdateTool,
    // Product selection tools
    productSelectionCreateTool,
    productSelectionReadTool,
    productSelectionUpdateTool,
    // Product tailoring tools
    productTailoringCreateTool,
    productTailoringReadTool,
    productTailoringUpdateTool,
    // Product type tools
    productTypeCreateTool,
    productTypeReadTool,
    productTypeUpdateTool,
    // Project tools
    projectReadTool,
    // Order tools
    orderCreateTool,
    orderReadTool,
    orderUpdateTool,
    // Payment tools
    paymentCreateTool,
    paymentReadTool,
    paymentUpdateTool,
];


server.setRequestHandler(ListToolsRequestSchema, async () => {
    return {
        tools,
    };
});


server.setRequestHandler(CallToolRequestSchema, async (request) => {
    const { name, arguments: args } = request.params;

    try {
        switch (name) {
            // Customer tools
            case 'customer_create':
                return await handleCustomerCreate(args);
            case 'customer_read':
                return await handleCustomerRead(args);
            case 'customer_update':
                return await handleCustomerUpdate(args);
            case 'customer_group_create':
                return await handleCustomerGroupCreate(args);
            case 'customer_group_read':
                return await handleCustomerGroupRead(args);
            case 'customer_group_update':
                return await handleCustomerGroupUpdate(args);

            // Product search
            case 'product_search_read':
                return await handleProductSearch(args);

            // Products tools
            case 'products_create':
                return await handleProductsCreate(args);
            case 'products_read':
                return await handleProductsRead(args);
            case 'products_update':
                return await handleProductsUpdate(args);

            // Product selection tools
            case 'product_selection_create':
                return await handleProductSelectionCreate(args);
            case 'product_selection_read':
                return await handleProductSelectionRead(args);
            case 'product_selection_update':
                return await handleProductSelectionUpdate(args);

            // Product tailoring tools
            case 'product_tailoring_create':
                return await handleProductTailoringCreate(args);
            case 'product_tailoring_read':
                return await handleProductTailoringRead(args);
            case 'product_tailoring_update':
                return await handleProductTailoringUpdate(args);

            // Product type tools
            case 'product_type_create':
                return await handleProductTypeCreate(args);
            case 'product_type_read':
                return await handleProductTypeRead(args);
            case 'product_type_update':
                return await handleProductTypeUpdate(args);

            // Project tools
            case 'project_read':
                return await handleProjectRead(args);

            // Order tools
            case 'order_create':
                return await handleOrderCreate(args);
            case 'order_read':
                return await handleOrderRead(args);
            case 'order_update':
                return await handleOrderUpdate(args);

            // Payment tools
            case 'payment_create':
                return await handlePaymentCreate(args);
            case 'payment_read':
                return await handlePaymentRead(args);
            case 'payment_update':
                return await handlePaymentUpdate(args);

            default:
                throw new Error(`Unknown tool: ${name}`);
        }
    } catch (error: any) {
        return {
            content: [
                {
                    type: 'text',
                    text: `Error executing tool ${name}: ${error.message}`,
                },
            ],
            isError: true,
        };
    }
});


async function main() {
    const transport = new StdioServerTransport();
    await server.connect(transport);
    console.error('commercetools MCP Tools Server running on stdio');
    console.error('Registered 26 tools: 6 customer + 13 product + 1 project + 3 order + 3 payment');
}

main().catch((error) => {
    console.error('Fatal error in main():', error);
    process.exit(1);
});
