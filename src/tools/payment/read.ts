import { apiRoot } from '../../config/client.js';

export const paymentReadTool = {
    name: 'payment_read',
    description: 'Query and retrieve payment information from commercetools.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Payment ID',
            },
            key: {
                type: 'string',
                description: 'Payment key',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter payments',
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
                description: 'Sort expression',
            },
            expand: {
                type: 'array',
                items: { type: 'string' },
                description: 'Fields to expand',
            },
        },
    },
};

export async function handlePaymentRead(args: any) {
    try {
        if (args.id) {
            const response = await apiRoot
                .payments()
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

        if (args.key) {
            const response = await apiRoot
                .payments()
                .withKey({ key: args.key })
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
            .payments()
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
                    text: `Error reading payment: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
