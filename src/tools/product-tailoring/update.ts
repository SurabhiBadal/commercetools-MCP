import { apiRoot } from '../../config/client.js';
import type { ProductTailoringUpdateAction } from '@commercetools/platform-sdk';

export const productTailoringUpdateTool = {
    name: 'product_tailoring_update',
    description: 'Update product tailoring. Supports actions like changing name, description, slug, and publishing.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Product tailoring ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Product tailoring key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions (e.g., setName, setDescription, setSlug, publish)',
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

export async function handleProductTailoringUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: ProductTailoringUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.productTailoring().withId({ ID: args.id })
            : apiRoot.productTailoring().withKey({ key: args.key });

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
                    text: `Error updating product tailoring: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
