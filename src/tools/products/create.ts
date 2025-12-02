import { apiRoot } from '../../config/client.js';
import type { ProductDraft } from '@commercetools/platform-sdk';

export const productsCreateTool = {
    name: 'products_create',
    description: 'Create a new product in commercetools with product variants, prices, and attributes.',
    inputSchema: {
        type: 'object',
        properties: {
            productType: {
                type: 'object',
                description: 'Reference to product type (use id or key)',
                properties: {
                    id: { type: 'string' },
                    key: { type: 'string' },
                },
            },
            name: {
                type: 'object',
                description: 'Localized product name (e.g., {"en": "Product Name"})',
            },
            slug: {
                type: 'object',
                description: 'Localized URL-friendly slug (e.g., {"en": "product-name"})',
            },
            description: {
                type: 'object',
                description: 'Localized product description',
            },
            categories: {
                type: 'array',
                description: 'Array of category references',
                items: {
                    type: 'object',
                    properties: {
                        id: { type: 'string' },
                        key: { type: 'string' },
                    },
                },
            },
            key: {
                type: 'string',
                description: 'Unique key for the product',
            },
            masterVariant: {
                type: 'object',
                description: 'Master product variant',
                properties: {
                    sku: { type: 'string' },
                    key: { type: 'string' },
                    prices: { type: 'array' },
                    attributes: { type: 'array' },
                },
            },
            variants: {
                type: 'array',
                description: 'Additional product variants',
                items: { type: 'object' },
            },
            publish: {
                type: 'boolean',
                description: 'Whether to publish the product immediately',
                default: false,
            },
        },
        required: ['productType', 'name', 'slug'],
    },
};

export async function handleProductsCreate(args: any) {
    try {
        const productDraft: ProductDraft = {
            productType: {
                typeId: 'product-type',
                ...(args.productType.id ? { id: args.productType.id } : { key: args.productType.key }),
            },
            name: args.name,
            slug: args.slug,
            description: args.description,
            categories: args.categories?.map((cat: any) => ({
                typeId: 'category',
                ...(cat.id ? { id: cat.id } : { key: cat.key }),
            })),
            key: args.key,
            masterVariant: args.masterVariant,
            variants: args.variants,
            publish: args.publish,
        };

        const response = await apiRoot
            .products()
            .post({ body: productDraft })
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
                    text: `Error creating product: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
