import { apiRoot } from '../../config/client.js';
import type { ProductTypeDraft } from '@commercetools/platform-sdk';

export const productTypeCreateTool = {
    name: 'product_type_create',
    description: 'Create a new product type in commercetools. Product types define the structure and attributes of products.',
    inputSchema: {
        type: 'object',
        properties: {
            name: {
                type: 'string',
                description: 'Name of the product type (required)',
            },
            description: {
                type: 'string',
                description: 'Description of the product type',
            },
            key: {
                type: 'string',
                description: 'Unique key for the product type',
            },
            attributes: {
                type: 'array',
                description: 'Array of attribute definitions',
                items: {
                    type: 'object',
                    properties: {
                        name: { type: 'string', description: 'Attribute name' },
                        label: { type: 'object', description: 'Localized label' },
                        type: { type: 'object', description: 'Attribute type definition' },
                        isRequired: { type: 'boolean', description: 'Whether attribute is required' },
                        isSearchable: { type: 'boolean', description: 'Whether attribute is searchable' },
                    },
                    required: ['name', 'label', 'type'],
                },
            },
        },
        required: ['name'],
    },
};

export async function handleProductTypeCreate(args: any) {
    try {
        const productTypeDraft: ProductTypeDraft = {
            name: args.name,
            description: args.description,
            key: args.key,
            attributes: args.attributes,
        };

        const response = await apiRoot
            .productTypes()
            .post({ body: productTypeDraft })
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
                    text: `Error creating product type: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
