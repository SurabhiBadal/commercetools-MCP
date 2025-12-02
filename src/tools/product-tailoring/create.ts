import { apiRoot } from '../../config/client.js';
import type { ProductTailoringDraft } from '@commercetools/platform-sdk';

export const productTailoringCreateTool = {
    name: 'product_tailoring_create',
    description: 'Create product tailoring to customize product data for specific stores without changing the original product.',
    inputSchema: {
        type: 'object',
        properties: {
            store: {
                type: 'object',
                description: 'Reference to the store',
                properties: {
                    id: { type: 'string' },
                    key: { type: 'string' },
                },
            },
            product: {
                type: 'object',
                description: 'Reference to the product',
                properties: {
                    id: { type: 'string' },
                    key: { type: 'string' },
                },
            },
            name: {
                type: 'object',
                description: 'Localized tailored product name',
            },
            description: {
                type: 'object',
                description: 'Localized tailored product description',
            },
            metaTitle: {
                type: 'object',
                description: 'Localized meta title for SEO',
            },
            metaDescription: {
                type: 'object',
                description: 'Localized meta description for SEO',
            },
            slug: {
                type: 'object',
                description: 'Localized tailored slug',
            },
            publish: {
                type: 'boolean',
                description: 'Whether to publish immediately',
                default: false,
            },
        },
        required: ['store', 'product'],
    },
};

export async function handleProductTailoringCreate(args: any) {
    try {
        const productTailoringDraft: ProductTailoringDraft = {
            store: {
                typeId: 'store',
                ...(args.store.id ? { id: args.store.id } : { key: args.store.key }),
            },
            product: {
                typeId: 'product',
                ...(args.product.id ? { id: args.product.id } : { key: args.product.key }),
            },
            name: args.name,
            description: args.description,
            metaTitle: args.metaTitle,
            metaDescription: args.metaDescription,
            slug: args.slug,
            publish: args.publish,
        };

        const response = await apiRoot
            .productTailoring()
            .post({ body: productTailoringDraft })
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
                    text: `Error creating product tailoring: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
