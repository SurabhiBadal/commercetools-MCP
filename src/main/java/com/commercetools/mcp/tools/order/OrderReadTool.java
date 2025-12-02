package com.commercetools.mcp.tools.order;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderReadTool extends AbstractCommercetoolsTool {
    public OrderReadTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("order_read")
                .description("Query orders by ID, order number, or predicates")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "orderNumber", Map.of("type", "string"),
                        "where", Map.of("type", "string"),
                        "limit", Map.of("type", "number", "default", 20),
                        "offset", Map.of("type", "number", "default", 0)
                )))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (args.containsKey("id")) {
                Order order = apiRoot.orders().withId((String) args.get("id")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(order));
            }
            if (args.containsKey("orderNumber")) {
                Order order = apiRoot.orders().withOrderNumber((String) args.get("orderNumber")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(order));
            }
            var query = apiRoot.orders().get();
            if (args.containsKey("where")) query.withWhere((String) args.get("where"));
            if (args.containsKey("limit")) query.withLimit(((Number) args.get("limit")).intValue());
            if (args.containsKey("offset")) query.withOffset(((Number) args.get("offset")).intValue());
            OrderPagedQueryResponse response = query.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("order_read", e);
        }
    }
}
