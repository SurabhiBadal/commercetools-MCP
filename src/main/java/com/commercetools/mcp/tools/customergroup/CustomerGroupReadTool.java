package com.commercetools.mcp.tools.customergroup;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomerGroupReadTool extends AbstractCommercetoolsTool {

    public CustomerGroupReadTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_group_read")
                .description("Query and retrieve customer group information from commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "id", Map.of("type", "string"),
                                "key", Map.of("type", "string"),
                                "where", Map.of("type", "string"),
                                "limit", Map.of("type", "number", "default", 20),
                                "offset", Map.of("type", "number", "default", 0)
                        )
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (args.containsKey("id")) {
                CustomerGroup group = apiRoot.customerGroups()
                        .withId((String) args.get("id"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(group));
            }

            if (args.containsKey("key")) {
                CustomerGroup group = apiRoot.customerGroups()
                        .withKey((String) args.get("key"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(group));
            }

            var queryBuilder = apiRoot.customerGroups().get();
            if (args.containsKey("where")) {
                queryBuilder.withWhere((String) args.get("where"));
            }
            if (args.containsKey("limit")) {
                queryBuilder.withLimit(((Number) args.get("limit")).intValue());
            }
            if (args.containsKey("offset")) {
                queryBuilder.withOffset(((Number) args.get("offset")).intValue());
            }

            CustomerGroupPagedQueryResponse response = queryBuilder.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("customer_group_read", e);
        }
    }
}
