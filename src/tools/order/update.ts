import { apiRoot } from '../../config/client.js';
import type { OrderUpdateAction } from '@commercetools/platform-sdk';

export const orderUpdateTool = {
    name: 'order_update',
    description: 'Update an existing order. Supports actions like changing order state, adding payments, updating shipment status, etc.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Order ID (use either id or orderNumber)',
            },
            orderNumber: {
                type: 'string',
                description: 'Order number (use either id or orderNumber)',
            },
            version: {
                type: 'number',
                description: 'Current version (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions (e.g., changeOrderState, changePaymentState, changeShipmentState, addDelivery)',
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

export async function handleOrderUpdate(args: any) {
    try {
        if (!args.id && !args.orderNumber) {
            throw new Error('Either id or orderNumber must be provided');
        }

        const updateActions: OrderUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.orders().withId({ ID: args.id })
            : apiRoot.orders().withOrderNumber({ orderNumber: args.orderNumber });

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
                    text: `Error updating order: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
