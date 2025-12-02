import { apiRoot } from '../../config/client.js';

export const orderReadTool = {
    name: 'order_read',
    description: 'Query and retrieve order information from commercetools. Can search by ID, order number, or use query predicates.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Order ID',
            },
            orderNumber: {
                type: 'string',
                description: 'Order number',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter orders',
            },
            limit: {
                type: 'number',
                description: 'Maximum number of results (default: 20)',
                default: 20,
            },
            offset: {
                type: 'number',
                description: 'Number of results to skip (default: 0)',
                default: 0,
            },
            sort: {
                type: 'string',
                description: 'Sort expression (e.g., "createdAt desc")',
            },
            expand: {
                type: 'array',
                items: { type: 'string' },
                description: 'Fields to expand',
            },
        },
    },
};

export async function handleOrderRead(args: any) {
    try {
        if (args.id) {
            const response = await apiRoot
                .orders()
                .withId({ ID: args.id })
                .get({ queryArgs: { expand: args.expand } })
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

        if (args.orderNumber) {
            const response = await apiRoot
                .orders()
                .withOrderNumber({ orderNumber: args.orderNumber })
                .get({ queryArgs: { expand: args.expand } })
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

        const queryArgs: any = {
            limit: args.limit || 20,
            offset: args.offset || 0,
        };

        if (args.where) queryArgs.where = args.where;
        if (args.sort) queryArgs.sort = args.sort;
        if (args.expand) queryArgs.expand = args.expand;

        const response = await apiRoot
            .orders()
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
                    text: `Error reading order: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
