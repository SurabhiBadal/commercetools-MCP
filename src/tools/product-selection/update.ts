import { apiRoot } from '../../config/client.js';
import type { ProductSelectionUpdateAction } from '@commercetools/platform-sdk';

export const productSelectionUpdateTool = {
    name: 'product_selection_update',
    description: 'Update an existing product selection. Supports actions like changing name, adding/removing products, and setting mode.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product selection ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Product selection key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions (e.g., changeName, addProduct, removeProduct, setKey)',
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

export async function handleProductSelectionUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: ProductSelectionUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.productSelections().withId({ ID: args.id })
            : apiRoot.productSelections().withKey({ key: args.key });

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
                    text: `Error updating product selection: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
