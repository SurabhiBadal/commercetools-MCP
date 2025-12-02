import { apiRoot } from '../../config/client.js';
import type { ProductTypeUpdateAction } from '@commercetools/platform-sdk';

export const productTypeUpdateTool = {
    name: 'product_type_update',
    description: 'Update an existing product type. Supports actions like changing name, description, and managing attributes.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product type ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Product type key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions (e.g., changeName, setDescription, addAttributeDefinition, removeAttributeDefinition)',
                items: {
                    type: 'object',
                    properties: {
                        action: { type: 'string' },
                    },
                    required: ['action'],
                },
            },
        },
        required: ['version', 'actions'],
    },
};

export async function handleProductTypeUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: ProductTypeUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.productTypes().withId({ ID: args.id })
            : apiRoot.productTypes().withKey({ key: args.key });

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
                    text: `Error updating product type: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
