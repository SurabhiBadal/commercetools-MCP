package com.commercetools.mcp.tools.producttype;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import com.commercetools.api.models.product_type.ProductTypeUpdate;
import com.commercetools.api.models.product_type.ProductTypeUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductTypeUpdateTool extends AbstractCommercetoolsTool {
    public ProductTypeUpdateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_type_update")
                .description("Update a product type")
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
            List<ProductTypeUpdateAction> actions = actionMaps.stream()
                    .map(m -> objectMapper.convertValue(m, ProductTypeUpdateAction.class)).toList();
            ProductTypeUpdate update = ProductTypeUpdate.builder().version(version).actions(actions).build();
            ProductType pt = args.containsKey("id") ?
                    apiRoot.productTypes().withId((String) args.get("id")).post(update).executeBlocking().getBody() :
                    apiRoot.productTypes().withKey((String) args.get("key")).post(update).executeBlocking().getBody();
            return ToolResult.success(toJson(pt));
        } catch (Exception e) {
            return handleError("product_type_update", e);
        }
    }
}
