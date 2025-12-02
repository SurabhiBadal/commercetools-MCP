package com.commercetools.mcp.tools.customer;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Tool for reading customer information from commercetools.
 */
@Component
public class CustomerReadTool extends AbstractCommercetoolsTool {

    public CustomerReadTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_read")
                .description("Query and retrieve customer information from commercetools. Can search by ID, key, email, or use query predicates.")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "id", Map.of("type", "string", "description", "Customer ID"),
                                "key", Map.of("type", "string", "description", "Customer key"),
                                "where", Map.of("type", "string", "description", "Query predicate"),
                                "limit", Map.of("type", "number", "description", "Maximum results (default: 20)", "default", 20),
                                "offset", Map.of("type", "number", "description", "Results to skip (default: 0)", "default", 0),
                                "sort", Map.of("type", "string", "description", "Sort expression"),
                                "expand", Map.of("type", "array", "items", Map.of("type", "string"), "description", "Fields to expand")
                        )
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            // Get by ID
            if (args.containsKey("id")) {
                Customer customer = apiRoot.customers()
                        .withId((String) args.get("id"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(customer));
            }

            // Get by key
            if (args.containsKey("key")) {
                Customer customer = apiRoot.customers()
                        .withKey((String) args.get("key"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(customer));
            }

            // Query customers
            var queryBuilder = apiRoot.customers().get();

            if (args.containsKey("where")) {
                queryBuilder.withWhere((String) args.get("where"));
            }
            if (args.containsKey("limit")) {
                queryBuilder.withLimit(((Number) args.get("limit")).intValue());
            }
            if (args.containsKey("offset")) {
                queryBuilder.withOffset(((Number) args.get("offset")).intValue());
            }
            if (args.containsKey("sort")) {
                queryBuilder.withSort((String) args.get("sort"));
            }

            CustomerPagedQueryResponse response = queryBuilder
                    .executeBlocking()
                    .getBody();

            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("customer_read", e);
        }
    }
}
