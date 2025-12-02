import { apiRoot } from '../../config/client.js';
import type { CustomerGroupDraft } from '@commercetools/platform-sdk';

export const customerGroupCreateTool = {
    name: 'customer_group_create',
    description: 'Create a new customer group in commercetools. Customer groups can be used to organize customers and apply group-specific pricing.',
    inputSchema: {
        type: 'object',
        properties: {
            groupName: {
                type: 'string',
                description: 'Name of the customer group (required)',
            },
            key: {
                type: 'string',
                description: 'Unique key for the customer group',
            },
        },
        required: ['groupName'],
    },
};

export async function handleCustomerGroupCreate(args: any) {
    try {
        const customerGroupDraft: CustomerGroupDraft = {
            groupName: args.groupName,
            key: args.key,
        };

        const response = await apiRoot
            .customerGroups()
            .post({ body: customerGroupDraft })
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
                    text: `Error creating customer group: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
