import { apiRoot } from '../../config/client.js';

export const productsReadTool = {
    name: 'products_read',
    description: 'Query and retrieve product information from commercetools. Can search by ID, key, or use query predicates.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product ID to retrieve a specific product',
            },
            key: {
                type: 'string',
                description: 'Product key to retrieve a specific product',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter products',
            },
            limit: {
                type: 'number',
                description: 'Maximum number of results (default: 20, max: 500)',
                default: 20,
            },
            offset: {
                type: 'number',
                description: 'Number of results to skip (default: 0)',
                default: 0,
            },
            sort: {
                type: 'string',
                description: 'Sort expression',
            },
            expand: {
                type: 'array',
                items: { type: 'string' },
                description: 'Fields to expand (e.g., ["productType", "categories[*]"])',
            },
        },
    },
};

export async function handleProductsRead(args: any) {
    try {
        // Get product by ID
        if (args.id) {
            const response = await apiRoot
                .products()
                .withId({ ID: args.id })
                .get({
                    queryArgs: {
                        expand: args.expand,
                    },
                })
                .execute();

            return {
                content: [
                    {
                        type: 'text',
                        text: JSON.stringify(response.body, null, 2),
                    },
                ],
            };
        }

        // Get product by key
        if (args.key) {
            const response = await apiRoot
                .products()
                .withKey({ key: args.key })
                .get({
                    queryArgs: {
                        expand: args.expand,
                    },
                })
                .execute();

            return {
                content: [
                    {
                        type: 'text',
                        text: JSON.stringify(response.body, null, 2),
                    },
                ],
            };
        }

        // Query products
        const queryArgs: any = {
            limit: args.limit || 20,
            offset: args.offset || 0,
        };

        if (args.where) {
            queryArgs.where = args.where;
        }

        if (args.sort) {
            queryArgs.sort = args.sort;
        }

        if (args.expand) {
            queryArgs.expand = args.expand;
        }

        const response = await apiRoot
            .products()
            .get({ queryArgs })
            .execute();

        return {
            content: [
                {
                    type: 'text',
                    text: JSON.stringify(response.body, null, 2),
                },
            ],
        };
    } catch (error: any) {
        return {
            content: [
                {
                    type: 'text',
                    text: `Error reading product: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
