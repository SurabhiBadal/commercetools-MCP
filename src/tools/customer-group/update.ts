import { apiRoot } from '../../config/client.js';
import type { CustomerGroupUpdateAction } from '@commercetools/platform-sdk';

export const customerGroupUpdateTool = {
    name: 'customer_group_update',
    description: 'Update an existing customer group in commercetools. Supports actions like changing name, setting key, and custom fields.',
    inputSchema: {
        type: 'object',
        properties: {
            id: {
                type: 'string',
                description: 'Customer group ID (use either id or key)',
            },
            key: {
                type: 'string',
                description: 'Customer group key (use either id or key)',
            },
            version: {
                type: 'number',
                description: 'Current version of the customer group (required for optimistic concurrency control)',
            },
            actions: {
                type: 'array',
                description: 'Array of update actions to perform',
                items: {
                    type: 'object',
                    properties: {
                        action: {
                            type: 'string',
                            description: 'Action type (e.g., changeName, setKey, setCustomType, setCustomField)',
                        },
                    },
                    required: ['action'],
                },
            },
        },
        required: ['version', 'actions'],
    },
};

export async function handleCustomerGroupUpdate(args: any) {
    try {
        if (!args.id && !args.key) {
            throw new Error('Either id or key must be provided');
        }

        const updateActions: CustomerGroupUpdateAction[] = args.actions;

        const requestBuilder = args.id
            ? apiRoot.customerGroups().withId({ ID: args.id })
            : apiRoot.customerGroups().withKey({ key: args.key });

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
                    text: `Error updating customer group: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
