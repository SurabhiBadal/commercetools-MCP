import { ClientBuilder } from '@commercetools/sdk-client-v2';
import { createApiBuilderFromCtpClient } from '@commercetools/platform-sdk';
import type { ByProjectKeyRequestBuilder } from '@commercetools/platform-sdk';
import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

// Validate required environment variables
const requiredEnvVars = [
    'CTP_PROJECT_KEY',
    'CTP_CLIENT_ID',
    'CTP_CLIENT_SECRET',
    'CTP_AUTH_URL',
    'CTP_API_URL'
];

for (const envVar of requiredEnvVars) {
    if (!process.env[envVar]) {
        throw new Error(`Missing required environment variable: ${envVar}`);
    }
}

// Create commercetools client with authentication
const client = new ClientBuilder()
    .withProjectKey(process.env.CTP_PROJECT_KEY!)
    .withClientCredentialsFlow({
        host: process.env.CTP_AUTH_URL!,
        projectKey: process.env.CTP_PROJECT_KEY!,
        credentials: {
            clientId: process.env.CTP_CLIENT_ID!,
            clientSecret: process.env.CTP_CLIENT_SECRET!,
        },
    })
    .withHttpMiddleware({
        host: process.env.CTP_API_URL!,
    })
    .withLoggerMiddleware()
    .build();

// Create API request builder
export const apiRoot: ByProjectKeyRequestBuilder = createApiBuilderFromCtpClient(client)
    .withProjectKey({ projectKey: process.env.CTP_PROJECT_KEY! });

export const projectKey = process.env.CTP_PROJECT_KEY!;
