package com.commercetools.mcp.tools.customergroup;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomerGroupCreateTool extends AbstractCommercetoolsTool {

    public CustomerGroupCreateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_group_create")
                .description("Create a new customer group in commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "groupName", Map.of("type", "string", "description", "Name of the customer group (required)"),
                                "key", Map.of("type", "string", "description", "Unique key for the customer group")
                        ),
                        "required", List.of("groupName")
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            var builder = CustomerGroupDraft.builder()
                    .groupName((String) args.get("groupName"));

            if (args.containsKey("key")) {
                builder.key((String) args.get("key"));
            }

            CustomerGroup group = apiRoot.customerGroups()
                    .post(builder.build())
                    .executeBlocking()
                    .getBody();

            return ToolResult.success(toJson(group));
        } catch (Exception e) {
            return handleError("customer_group_create", e);
        }
    }
}
