import { apiRoot } from '../../config/client.js';
import type { CustomerDraft } from '@commercetools/platform-sdk';

export const customerCreateTool = {
    name: 'customer_create',
    description: 'Create a new customer in commercetools. This will sign up a new customer with the provided details.',
    inputSchema: {
        type: 'object',
        properties: {
            email: {
                type: 'string',
                description: 'Customer email address (required, must be unique)',
            },
            password: {
                type: 'string',
                description: 'Customer password (required for password authentication)',
            },
            firstName: {
                type: 'string',
                description: 'Customer first name',
            },
            lastName: {
                type: 'string',
                description: 'Customer last name',
            },
            middleName: {
                type: 'string',
                description: 'Customer middle name',
            },
            title: {
                type: 'string',
                description: 'Customer title (e.g., Mr., Mrs., Dr.)',
            },
            dateOfBirth: {
                type: 'string',
                description: 'Date of birth in YYYY-MM-DD format',
            },
            companyName: {
                type: 'string',
                description: 'Company name',
            },
            vatId: {
                type: 'string',
                description: 'VAT ID',
            },
            customerGroupKey: {
                type: 'string',
                description: 'Key of the customer group to assign',
            },
            key: {
                type: 'string',
                description: 'Unique key for the customer',
            },
        },
        required: ['email', 'password'],
    },
};

export async function handleCustomerCreate(args: any) {
    try {
        const customerDraft: CustomerDraft = {
            email: args.email,
            password: args.password,
            firstName: args.firstName,
            lastName: args.lastName,
            middleName: args.middleName,
            title: args.title,
            dateOfBirth: args.dateOfBirth,
            companyName: args.companyName,
            vatId: args.vatId,
            key: args.key,
            ...(args.customerGroupKey && {
                customerGroup: {
                    typeId: 'customer-group',
                    key: args.customerGroupKey,
                },
            }),
        };

        const response = await apiRoot
            .customers()
            .post({ body: customerDraft })
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
                    text: `Error creating customer: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
