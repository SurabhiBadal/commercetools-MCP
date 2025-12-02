package com.commercetools.mcp.tools.producttailoring;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_tailoring.ProductTailoring;
import com.commercetools.api.models.product_tailoring.ProductTailoringUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductTailoringUpdateTool extends AbstractCommercetoolsTool {
    public ProductTailoringUpdateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_tailoring_update")
                .description("Update product tailoring")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "key", Map.of("type", "string"),
                        "version", Map.of("type", "number"),
                        "actions", Map.of("type", "array", "items", Map.of("type", "object"))), "required",
                        List.of("version", "actions")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (!args.containsKey("id") && !args.containsKey("key"))
                return ToolResult.error("Either id or key required");
            long version = ((Number) args.get("version")).longValue();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) args.get("actions");
            List<ProductTailoringUpdateAction> actions = actionMaps.stream()
                    .map(m -> objectMapper.convertValue(m, ProductTailoringUpdateAction.class)).toList();

            // Create update inline since ProductTailoringUpdate builder may not be
            // available
            ProductTailoring pt = args.containsKey("id") ? apiRoot.productTailoring().withId((String) args.get("id"))
                    .post(builder -> builder.version(version).actions(actions))
                    .executeBlocking().getBody()
                    : apiRoot.productTailoring().withKey((String) args.get("key"))
                            .post(builder -> builder.version(version).actions(actions))
                            .executeBlocking().getBody();
            return ToolResult.success(toJson(pt));
        } catch (Exception e) {
            return handleError("product_tailoring_update", e);
        }
    }
}
