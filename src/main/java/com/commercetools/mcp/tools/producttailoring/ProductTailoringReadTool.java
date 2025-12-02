package com.commercetools.mcp.tools.producttailoring;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_tailoring.ProductTailoring;
import com.commercetools.api.models.product_tailoring.ProductTailoringPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductTailoringReadTool extends AbstractCommercetoolsTool {
    public ProductTailoringReadTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_tailoring_read")
                .description("Query product tailoring")
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
                ProductTailoring pt = apiRoot.productTailoring().withId((String) args.get("id")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(pt));
            }
            if (args.containsKey("key")) {
                ProductTailoring pt = apiRoot.productTailoring().withKey((String) args.get("key")).get().executeBlocking().getBody();
                return ToolResult.success(toJson(pt));
            }
            var query = apiRoot.productTailoring().get();
            if (args.containsKey("where")) query.withWhere((String) args.get("where"));
            if (args.containsKey("limit")) query.withLimit(((Number) args.get("limit")).intValue());
            ProductTailoringPagedQueryResponse response = query.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("product_tailoring_read", e);
        }
    }
}
