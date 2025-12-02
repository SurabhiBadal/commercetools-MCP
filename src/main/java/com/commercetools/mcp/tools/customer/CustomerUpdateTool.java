package com.commercetools.mcp.tools.customer;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerUpdate;
import com.commercetools.api.models.customer.CustomerUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Tool for updating customer information in commercetools.
 */
@Component
public class CustomerUpdateTool extends AbstractCommercetoolsTool {

    public CustomerUpdateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("customer_update")
                .description("Update an existing customer in commercetools. Supports various update actions.")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "id", Map.of("type", "string", "description", "Customer ID (use either id or key)"),
                                "key", Map.of("type", "string", "description", "Customer key (use either id or key)"),
                                "version", Map.of("type", "number", "description", "Current version (required)"),
                                "actions", Map.of("type", "array", "description", "Array of update actions", "items", Map.of("type", "object"))
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
            
            // Convert action maps to CustomerUpdateAction objects
            List<CustomerUpdateAction> actions = actionMaps.stream()
                    .map(actionMap -> objectMapper.convertValue(actionMap, CustomerUpdateAction.class))
                    .toList();

            CustomerUpdate update = CustomerUpdate.builder()
                    .version(version)
                    .actions(actions)
                    .build();

            Customer customer;
            if (args.containsKey("id")) {
                customer = apiRoot.customers()
                        .withId((String) args.get("id"))
                        .post(update)
                        .executeBlocking()
                        .getBody();
            } else {
                customer = apiRoot.customers()
                        .withKey((String) args.get("key"))
                        .post(update)
                        .executeBlocking()
                        .getBody();
            }

            return ToolResult.success(toJson(customer));
        } catch (Exception e) {
            return handleError("customer_update", e);
        }
    }
}
