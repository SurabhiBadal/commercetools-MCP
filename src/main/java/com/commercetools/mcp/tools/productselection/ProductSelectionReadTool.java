package com.commercetools.mcp.tools.productselection;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_selection.ProductSelection;
import com.commercetools.api.models.product_selection.ProductSelectionPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductSelectionReadTool extends AbstractCommercetoolsTool {
    public ProductSelectionReadTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_selection_read")
                .description("Query product selections")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "id", Map.of("type", "string"),
                        "key", Map.of("type", "string"),
                        "where", Map.of("type", "string"),
                        "limit", Map.of("type", "number", "default", 20)
                )))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (args.containsKey("id")) {
                ProductSelection ps = apiRoot.productSelections().withId((String) args.get("id")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(ps));
            }
            if (args.containsKey("key")) {
                ProductSelection ps = apiRoot.productSelections().withKey((String) args.get("key")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(ps));
            }
            var query = apiRoot.productSelections().get();
            if (args.containsKey("where")) query.withWhere((String) args.get("where"));
            if (args.containsKey("limit")) query.withLimit(((Number) args.get("limit")).intValue());
            ProductSelectionPagedQueryResponse response = query.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("product_selection_read", e);
        }
    }
}
