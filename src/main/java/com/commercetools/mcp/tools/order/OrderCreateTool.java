package com.commercetools.mcp.tools.order;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderCreateTool extends AbstractCommercetoolsTool {
    public OrderCreateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("order_create")
                .description("Create an order from a cart, quote, or by importing")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "cart", Map.of("type", "object", "description", "Cart reference (id or key)"),
                        "version", Map.of("type", "number", "description", "Cart version"),
                        "orderNumber", Map.of("type", "string"),
                        "paymentState", Map.of("type", "string"),
                        "shipmentState", Map.of("type", "string")
                ), "required", List.of("cart", "version")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            OrderFromCartDraft draft = objectMapper.convertValue(args, OrderFromCartDraft.class);
            Order order = apiRoot.orders().post(draft).executeBlocking().getBody();
            return ToolResult.success(toJson(order));
        } catch (Exception e) {
            return handleError("order_create", e);
        }
    }
}
