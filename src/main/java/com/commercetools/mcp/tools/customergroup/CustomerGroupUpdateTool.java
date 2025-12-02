package com.commercetools.mcp.tools.customergroup;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupUpdate;
import com.commercetools.api.models.customer_group.CustomerGroupUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomerGroupUpdateTool extends AbstractCommercetoolsTool {

    public CustomerGroupUpdateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_group_update")
                .description("Update an existing customer group in commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "id", Map.of("type", "string"),
                                "key", Map.of("type", "string"),
                                "version", Map.of("type", "number"),
                                "actions", Map.of("type", "array", "items", Map.of("type", "object"))
                        ),
                        "required", List.of("version", "actions")
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (!args.containsKey("id") && !args.containsKey("key")) {
                return ToolResult.error("Either id or key must be provided");
            }

            long version = ((Number) args.get("version")).longValue();
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) args.get("actions");
            List<CustomerGroupUpdateAction> actions = actionMaps.stream()
                    .map(actionMap -> objectMapper.convertValue(actionMap, CustomerGroupUpdateAction.class))
                    .toList();

            CustomerGroupUpdate update = CustomerGroupUpdate.builder()
                    .version(version)
                    .actions(actions)
                    .build();

            CustomerGroup group;
            if (args.containsKey("id")) {
                group = apiRoot.customerGroups()
                        .withId((String) args.get("id"))
                        .post(update)
                        .executeBlocking()
                        .getBody();
            } else {
                group = apiRoot.customerGroups()
                        .withKey((String) args.get("key"))
                        .post(update)
                        .executeBlocking()
                        .getBody();
            }

            return ToolResult.success(toJson(group));
        } catch (Exception e) {
            return handleError("customer_group_update", e);
        }
    }
}
