import { apiRoot } from '../../config/client.js';

export const productTailoringReadTool = {
    name: 'product_tailoring_read',
    description: 'Query and retrieve product tailoring information from commercetools.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product tailoring ID',
            },
            key: {
                type: 'string',
                description: 'Product tailoring key',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter product tailoring',
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
            expand: {
                type: 'array',
                items: { type: 'string' },
                description: 'Fields to expand',
            },
        },
    },
};

export async function handleProductTailoringRead(args: any) {
    try {
        if (args.id) {
            const response = await apiRoot
                .productTailoring()
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
                .productTailoring()
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
        if (args.expand) queryArgs.expand = args.expand;

        const response = await apiRoot
            .productTailoring()
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
                    text: `Error reading product tailoring: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
