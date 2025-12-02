package com.commercetools.mcp.tools.productselection;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_selection.ProductSelection;
import com.commercetools.api.models.product_selection.ProductSelectionUpdate;
import com.commercetools.api.models.product_selection.ProductSelectionUpdateAction;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductSelectionUpdateTool extends AbstractCommercetoolsTool {
    public ProductSelectionUpdateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_selection_update")
                .description("Update a product selection")
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
            if (!args.containsKey("id") && !args.containsKey("key")) {
                return ToolResult.error("Either id or key required");
            }
            long version = ((Number) args.get("version")).longValue();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) args.get("actions");
            List<ProductSelectionUpdateAction> actions = actionMaps.stream()
                    .map(m -> objectMapper.convertValue(m, ProductSelectionUpdateAction.class)).toList();
            ProductSelectionUpdate update = ProductSelectionUpdate.builder().version(version).actions(actions).build();
            ProductSelection ps = args.containsKey("id") ?
                    apiRoot.productSelections().withId((String) args.get("id")).post(update).executeBlocking().getBody() :
                    apiRoot.productSelections().withKey((String) args.get("key")).post(update).executeBlocking().getBody();
            return ToolResult.success(toJson(ps));
        } catch (Exception e) {
            return handleError("product_selection_update", e);
        }
    }
}
