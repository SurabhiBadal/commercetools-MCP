import { apiRoot } from '../../config/client.js';

export const customerReadTool = {
    name: 'customer_read',
    description: 'Query and retrieve customer information from commercetools. Can search by ID, key, email, or use query predicates.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Customer ID to retrieve a specific customer',
            },
            key: {
                type: 'string',
                description: 'Customer key to retrieve a specific customer',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter customers (e.g., "email=\\"john@example.com\\"")',
            },
            limit: {
                type: 'number',
                description: 'Maximum number of results to return (default: 20, max: 500)',
                default: 20,
            },
            offset: {
                type: 'number',
                description: 'Number of results to skip for pagination (default: 0)',
                default: 0,
            },
            sort: {
                type: 'string',
                description: 'Sort expression (e.g., "createdAt desc")',
            },
            expand: {
                type: 'array',
                items: { type: 'string' },
                description: 'Fields to expand (e.g., ["customerGroup"])',
            },
        },
    },
};

export async function handleCustomerRead(args: any) {
    try {
        // Get customer by ID
        if (args.id) {
            const response = await apiRoot
                .customers()
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

        // Get customer by key
        if (args.key) {
            const response = await apiRoot
                .customers()
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

        // Query customers
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
            .customers()
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
                    text: `Error reading customer: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
