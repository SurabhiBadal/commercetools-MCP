import { apiRoot } from '../../config/client.js';

export const projectReadTool = {
    name: 'project_read',
    description: 'Read project information and settings from commercetools. Returns project configuration including currencies, languages, countries, and other settings.',
    inputSchema: {
        type: 'object',
        properties: {},
    },
};

export async function handleProjectRead(args: any) {
    try {
        const response = await apiRoot
            .get()
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
                    text: `Error reading project: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
