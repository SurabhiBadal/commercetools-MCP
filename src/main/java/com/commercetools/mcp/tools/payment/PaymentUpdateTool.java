package com.commercetools.mcp.tools.payment;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.payment.Payment;
import com.commercetools.api.models.payment.PaymentUpdate;
import com.commercetools.api.models.payment.PaymentUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PaymentUpdateTool extends AbstractCommercetoolsTool {
    public PaymentUpdateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("payment_update")
                .description("Update payment transactions and status")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "key", Map.of("type", "string"),
                        "version", Map.of("type", "number"),
                        "actions", Map.of("type", "array", "items", Map.of("type", "object"))
                ), "required", List.of("version", "actions")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (!args.containsKey("id") && !args.containsKey("key")) return ToolResult.error("Either id or key required");
            long version = ((Number) args.get("version")).longValue();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) args.get("actions");
            List<PaymentUpdateAction> actions = actionMaps.stream()
                    .map(m -> objectMapper.convertValue(m, PaymentUpdateAction.class)).toList();
            PaymentUpdate update = PaymentUpdate.builder().version(version).actions(actions).build();
            Payment payment = args.containsKey("id") ?
                    apiRoot.payments().withId((String) args.get("id")).post(update).executeBlocking().getBody() :
                    apiRoot.payments().withKey((String) args.get("key")).post(update).executeBlocking().getBody();
            return ToolResult.success(toJson(payment));
        } catch (Exception e) {
            return handleError("payment_update", e);
        }
    }
}
