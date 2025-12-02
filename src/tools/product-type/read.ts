import { apiRoot } from '../../config/client.js';

export const productTypeReadTool = {
    name: 'product_type_read',
    description: 'Query and retrieve product type information from commercetools.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product type ID',
            },
            key: {
                type: 'string',
                description: 'Product type key',
            },
            where: {
                type: 'string',
                description: 'Query predicate to filter product types',
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
        },
    },
};

export async function handleProductTypeRead(args: any) {
    try {
        if (args.id) {
            const response = await apiRoot
                .productTypes()
                .withId({ ID: args.id })
                .get()
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
                .productTypes()
                .withKey({ key: args.key })
                .get()
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

        const response = await apiRoot
            .productTypes()
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
                    text: `Error reading product type: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
