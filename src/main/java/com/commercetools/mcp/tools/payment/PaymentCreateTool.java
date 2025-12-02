package com.commercetools.mcp.tools.payment;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.payment.Payment;
import com.commercetools.api.models.payment.PaymentDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PaymentCreateTool extends AbstractCommercetoolsTool {
    public PaymentCreateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("payment_create")
                .description("Create a payment record with transactions")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "key", Map.of("type", "string"),
                        "customer", Map.of("type", "object"),
                        "amountPlanned", Map.of("type", "object", "description", "Amount with currencyCode and centAmount"),
                        "paymentMethodInfo", Map.of("type", "object"),
                        "transactions", Map.of("type", "array", "items", Map.of("type", "object"))
                ), "required", List.of("amountPlanned")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            PaymentDraft draft = objectMapper.convertValue(args, PaymentDraft.class);
            Payment payment = apiRoot.payments().post(draft).executeBlocking().getBody();
            return ToolResult.success(toJson(payment));
        } catch (Exception e) {
            return handleError("payment_create", e);
        }
    }
}
