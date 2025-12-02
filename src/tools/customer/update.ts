import { apiRoot } from '../../config/client.js';
import type { CustomerUpdateAction } from '@commercetools/platform-sdk';

export const customerUpdateTool = {
    name: 'customer_update',
    description: 'Update an existing customer in commercetools. Supports various update actions like changing name, email, addresses, etc.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Customer ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Customer key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version of the customer (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions to perform',
                items: {
                    type: 'object',
                    properties: {
                        action: {
                            type: 'string',
                            description: 'Action type (e.g., setFirstName, setLastName, changeEmail, addAddress, setDateOfBirth)',
                        },
                    },
                    required: ['action'],
                },
            },
        },
        required: ['version', 'actions'],
    },
};

export async function handleCustomerUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: CustomerUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.customers().withId({ ID: args.id })
            : apiRoot.customers().withKey({ key: args.key });

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
                    text: `Error updating customer: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
