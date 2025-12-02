import { apiRoot } from '../../config/client.js';
import type { OrderFromCartDraft } from '@commercetools/platform-sdk';

export const orderCreateTool = {
    name: 'order_create',
    description: 'Create an order from a cart, quote, or by importing order data. Most commonly used to convert a cart into an order.',
    inputSchema: {
        type: 'object',
        properties: {
            cart: {
                type: 'object',
                description: 'Reference to cart (use id or key)',
                properties: {
                    id: { type: 'string' },
                    key: { type: 'string' },
                },
            },
            version: {
                type: 'number',
                description: 'Version of the cart (required for cart-based orders)',
            },
            orderNumber: {
                type: 'string',
                description: 'Custom order number',
            },
            paymentState: {
                type: 'string',
                description: 'Payment state (e.g., Paid, Pending)',
            },
            shipmentState: {
                type: 'string',
                description: 'Shipment state (e.g., Shipped, Pending)',
            },
        },
    },
};

export async function handleOrderCreate(args: any) {
    try {
        const orderDraft: OrderFromCartDraft = {
            cart: {
                typeId: 'cart',
                ...(args.cart?.id ? { id: args.cart.id } : { key: args.cart?.key }),
            },
            version: args.version,
            orderNumber: args.orderNumber,
            paymentState: args.paymentState,
            shipmentState: args.shipmentState,
        };

        const response = await apiRoot
            .orders()
            .post({ body: orderDraft })
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
                    text: `Error creating order: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
