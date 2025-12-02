import { apiRoot } from '../../config/client.js';
import type { PaymentDraft } from '@commercetools/platform-sdk';

export const paymentCreateTool = {
    name: 'payment_create',
    description: 'Create a new payment in commercetools. Payments track payment transactions and can be associated with orders.',
    inputSchema: {
        type: 'object',
        properties: {
            key: {
                type: 'string',
                description: 'Unique key for the payment',
            },
            customer: {
                type: 'object',
                description: 'Reference to customer',
                properties: {
                    id: { type: 'string' },
                    key: { type: 'string' },
                },
            },
            amountPlanned: {
                type: 'object',
                description: 'Planned payment amount',
                properties: {
                    currencyCode: { type: 'string', description: 'Currency code (e.g., USD, EUR)' },
                    centAmount: { type: 'number', description: 'Amount in cents' },
                },
                required: ['currencyCode', 'centAmount'],
            },
            paymentMethodInfo: {
                type: 'object',
                description: 'Payment method information',
                properties: {
                    paymentInterface: { type: 'string' },
                    method: { type: 'string' },
                    name: { type: 'object', description: 'Localized payment method name' },
                },
            },
            transactions: {
                type: 'array',
                description: 'Payment transactions',
                items: { type: 'object' },
            },
        },
        required: ['amountPlanned'],
    },
};

export async function handlePaymentCreate(args: any) {
    try {
        const paymentDraft: PaymentDraft = {
            key: args.key,
            customer: args.customer ? {
                typeId: 'customer',
                ...(args.customer.id ? { id: args.customer.id } : { key: args.customer.key }),
            } : undefined,
            amountPlanned: args.amountPlanned,
            paymentMethodInfo: args.paymentMethodInfo,
            transactions: args.transactions,
        };

        const response = await apiRoot
            .payments()
            .post({ body: paymentDraft })
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
                    text: `Error creating payment: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
