package com.commercetools.mcp.tools.product;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductUpdate;
import com.commercetools.api.models.product.ProductUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductUpdateTool extends AbstractCommercetoolsTool {

    public ProductUpdateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("products_update")
                .description("Update an existing product in commercetools")
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
            List<ProductUpdateAction> actions = actionMaps.stream()
                    .map(actionMap -> objectMapper.convertValue(actionMap, ProductUpdateAction.class))
                    .toList();

            ProductUpdate update = ProductUpdate.builder()
                    .version(version)
                    .actions(actions)
                    .build();

            Product product;
            if (args.containsKey("id")) {
                product = apiRoot.products().withId((String) args.get("id")).post(update).executeBlocking().getBody();
            } else {
                product = apiRoot.products().withKey((String) args.get("key")).post(update).executeBlocking().getBody();
            }

            return ToolResult.success(toJson(product));
        } catch (Exception e) {
            return handleError("products_update", e);
        }
    }
}
