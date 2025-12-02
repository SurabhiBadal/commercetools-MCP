import { apiRoot } from '../../config/client.js';
import type { ProductUpdateAction } from '@commercetools/platform-sdk';

export const productsUpdateTool = {
    name: 'products_update',
    description: 'Update an existing product in commercetools. Supports various update actions like changing name, price, attributes, publishing, etc.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Product key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version of the product (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions to perform',
                items: {
                    type: 'object',
                    properties: {
                        action: {
                            type: 'string',
                            description: 'Action type (e.g., changeName, setDescription, addPrice, publish, unpublish)',
                        },
                    },
                    required: ['action'],
                },
            },
        },
        required: ['version', 'actions'],
    },
};

export async function handleProductsUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: ProductUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.products().withId({ ID: args.id })
            : apiRoot.products().withKey({ key: args.key });

        const response = await requestBuilder
            .post({
                body: {
                    version: args.version,
                    actions: updateActions,
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
    } catch (error: any) {
        return {
            content: [
                {
                    type: 'text',
                    text: `Error updating product: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
