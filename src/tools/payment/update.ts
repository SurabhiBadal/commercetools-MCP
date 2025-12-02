import { apiRoot } from '../../config/client.js';
import type { PaymentUpdateAction } from '@commercetools/platform-sdk';

export const paymentUpdateTool = {
    name: 'payment_update',
    description: 'Update an existing payment. Supports actions like adding transactions, changing amount, setting status, etc.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Payment ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Payment key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions (e.g., addTransaction, changeAmountPlanned, setStatusInterfaceCode)',
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

export async function handlePaymentUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: PaymentUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.payments().withId({ ID: args.id })
            : apiRoot.payments().withKey({ key: args.key });

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
                    text: `Error updating payment: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
