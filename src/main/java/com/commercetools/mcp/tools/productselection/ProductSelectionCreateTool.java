package com.commercetools.mcp.tools.productselection;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_selection.ProductSelection;
import com.commercetools.api.models.product_selection.ProductSelectionDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductSelectionCreateTool extends AbstractCommercetoolsTool {
    public ProductSelectionCreateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_selection_create")
                .description("Create a product selection")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "name", Map.of("type", "object"),
                        "key", Map.of("type", "string")
                ), "required", List.of("name")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            ProductSelectionDraft draft = objectMapper.convertValue(args, ProductSelectionDraft.class);
            ProductSelection result = apiRoot.productSelections().post(draft).executeBlocking().getBody();
            return ToolResult.success(toJson(result));
        } catch (Exception e) {
            return handleError("product_selection_create", e);
        }
    }
}
