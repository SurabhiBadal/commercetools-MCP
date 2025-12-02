package com.commercetools.mcp.tools.payment;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.payment.Payment;
import com.commercetools.api.models.payment.PaymentPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentReadTool extends AbstractCommercetoolsTool {
    public PaymentReadTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("payment_read")
                .description("Query payments by ID, key, or predicates")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "key", Map.of("type", "string"),
                        "where", Map.of("type", "string"),
                        "limit", Map.of("type", "number", "default", 20)
                )))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (args.containsKey("id")) {
                Payment payment = apiRoot.payments().withId((String) args.get("id")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(payment));
            }
            if (args.containsKey("key")) {
                Payment payment = apiRoot.payments().withKey((String) args.get("key")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(payment));
            }
            var query = apiRoot.payments().get();
            if (args.containsKey("where")) query.withWhere((String) args.get("where"));
            if (args.containsKey("limit")) query.withLimit(((Number) args.get("limit")).intValue());
            PaymentPagedQueryResponse response = query.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("payment_read", e);
        }
    }
}
