package com.commercetools.mcp.tools.order;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderUpdate;
import com.commercetools.api.models.order.OrderUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderUpdateTool extends AbstractCommercetoolsTool {
    public OrderUpdateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("order_update")
                .description("Update order state, payment status, shipment status")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "orderNumber", Map.of("type", "string"),
                        "version", Map.of("type", "number"),
                        "actions", Map.of("type", "array", "items", Map.of("type", "object"))
                ), "required", List.of("version", "actions")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (!args.containsKey("id") && !args.containsKey("orderNumber")) return ToolResult.error("Either id or orderNumber required");
            long version = ((Number) args.get("version")).longValue();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) args.get("actions");
            List<OrderUpdateAction> actions = actionMaps.stream()
                    .map(m -> objectMapper.convertValue(m, OrderUpdateAction.class)).toList();
            OrderUpdate update = OrderUpdate.builder().version(version).actions(actions).build();
            Order order = args.containsKey("id") ?
                    apiRoot.orders().withId((String) args.get("id")).post(update).executeBlocking().getBody() :
                    apiRoot.orders().withOrderNumber((String) args.get("orderNumber")).post(update).executeBlocking().getBody();
            return ToolResult.success(toJson(order));
        } catch (Exception e) {
            return handleError("order_update", e);
        }
    }
}
