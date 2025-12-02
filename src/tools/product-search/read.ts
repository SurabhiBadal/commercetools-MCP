import { apiRoot } from '../../config/client.js';

export const productSearchTool = {
    name: 'product_search_read',
    description: 'Search for products using the commercetools Product Search API. Supports full-text search, filtering, faceting, and fuzzy search.',
    inputSchema: {
        type: 'object',
        properties: {
            query: {
                type: 'object',
                description: 'Search query with text and filters',
                properties: {
                    text: {
                        type: 'string',
                        description: 'Full-text search query',
                    },
                    locale: {
                        type: 'string',
                        description: 'Locale for the search (e.g., "en-US")',
                    },
                },
            },
            filter: {
                type: 'array',
                description: 'Array of filter expressions',
                items: { type: 'string' },
            },
            facets: {
                type: 'array',
                description: 'Facets to calculate',
                items: { type: 'object' },
            },
            sort: {
                type: 'array',
                description: 'Sort expressions',
                items: { type: 'string' },
            },
            limit: {
                type: 'number',
                description: 'Maximum number of results (default: 20)',
                default: 20,
            },
            offset: {
                type: 'number',
                description: 'Number of results to skip (default: 0)',
                default: 0,
            },
        },
    },
};

export async function handleProductSearch(args: any) {
    try {
        const searchRequest: any = {
            limit: args.limit || 20,
            offset: args.offset || 0,
        };

        if (args.query) {
            searchRequest.query = args.query;
        }

        if (args.filter) {
            searchRequest.filter = args.filter;
        }

        if (args.facets) {
            searchRequest.facets = args.facets;
        }

        if (args.sort) {
            searchRequest.sort = args.sort;
        }

        const response = await apiRoot
            .productProjections()
            .search()
            .get({
                queryArgs: searchRequest,
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
                    text: `Error searching products: ${error.message}\n${JSON.stringify(error.body || {}, null, 2)}`,
                },
            ],
            isError: true,
        };
    }
}
