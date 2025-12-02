import { apiRoot } from '../../config/client.js';
import type { ProductSelectionDraft } from '@commercetools/platform-sdk';

export const productSelectionCreateTool = {
    name: 'product_selection_create',
    description: 'Create a new product selection in commercetools. Product selections are used to manage product assortments for different sales channels.',
    inputSchema: {
        type: 'object',
        properties: {
            name: {
                type: 'object',
                description: 'Localized name of the product selection (e.g., {"en": "VIP Selection"})',
            },
            key: {
                type: 'string',
                description: 'Unique key for the product selection',
            },
            mode: {
                type: 'string',
                description: 'Selection mode: "Individual" (include specific products) or "IndividualExclusion" (exclude specific products)',
                enum: ['Individual', 'IndividualExclusion'],
                default: 'Individual',
            },
        },
        required: ['name'],
    },
};

export async function handleProductSelectionCreate(args: any) {
    try {
        const productSelectionDraft: ProductSelectionDraft = {
            name: args.name,
            key: args.key,
            mode: args.mode || 'Individual',
        };

        const response = await apiRoot
            .productSelections()
            .post({ body: productSelectionDraft })
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
                    text: `Error creating product selection: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
