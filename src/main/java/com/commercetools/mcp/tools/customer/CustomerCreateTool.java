package com.commercetools.mcp.tools.customer;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer.CustomerDraftBuilder;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.commercetools.api.models.customer_group.CustomerGroupResourceIdentifier;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Tool for creating new customers in commercetools.
 */
@Component
public class CustomerCreateTool extends AbstractCommercetoolsTool {

    public CustomerCreateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        Map<String, Object> properties = new java.util.HashMap<>();
        properties.put("email",
                Map.of("type", "string", "description", "Customer email address (required, must be unique)"));
        properties.put("password", Map.of("type", "string", "description", "Customer password (required)"));
        properties.put("firstName", Map.of("type", "string", "description", "Customer first name"));
        properties.put("lastName", Map.of("type", "string", "description", "Customer last name"));
        properties.put("middleName", Map.of("type", "string", "description", "Customer middle name"));
        properties.put("title", Map.of("type", "string", "description", "Customer title"));
        properties.put("dateOfBirth", Map.of("type", "string", "description", "Date of birth (YYYY-MM-DD)"));
        properties.put("companyName", Map.of("type", "string", "description", "Company name"));
        properties.put("vatId", Map.of("type", "string", "description", "VAT ID"));
        properties.put("key", Map.of("type", "string", "description", "Unique key for the customer"));
        properties.put("customerGroupKey", Map.of("type", "string", "description", "Customer group key to assign"));

        return ToolDefinition.builder()
                .name("customer_create")
                .description(
                        "Create a new customer in commercetools. This will sign up a new customer with the provided details.")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", properties,
                        "required", List.of("email", "password")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            CustomerDraftBuilder builder = CustomerDraft.builder()
                    .email((String) args.get("email"))
                    .password((String) args.get("password"));

            if (args.containsKey("firstName")) {
                builder.firstName((String) args.get("firstName"));
            }
            if (args.containsKey("lastName")) {
                builder.lastName((String) args.get("lastName"));
            }
            if (args.containsKey("middleName")) {
                builder.middleName((String) args.get("middleName"));
            }
            if (args.containsKey("title")) {
                builder.title((String) args.get("title"));
            }
            if (args.containsKey("companyName")) {
                builder.companyName((String) args.get("companyName"));
            }
            if (args.containsKey("vatId")) {
                builder.vatId((String) args.get("vatId"));
            }
            if (args.containsKey("key")) {
                builder.key((String) args.get("key"));
            }
            if (args.containsKey("customerGroupKey")) {
                builder.customerGroup(CustomerGroupResourceIdentifier.builder()
                        .key((String) args.get("customerGroupKey"))
                        .build());
            }

            CustomerDraft draft = builder.build();
            CustomerSignInResult result = apiRoot.customers()
                    .post(draft)
                    .executeBlocking()
                    .getBody();

            return ToolResult.success(toJson(result));
        } catch (Exception e) {
            return handleError("customer_create", e);
        }
    }
}
