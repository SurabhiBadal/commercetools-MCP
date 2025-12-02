package com.commercetools.mcp.tools.product;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.ProductProjectionPagedSearchResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductSearchTool extends AbstractCommercetoolsTool {

    public ProductSearchTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_search_read")
                .description("Search products using full-text search, filtering, and faceting")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "text", Map.of("type", "string", "description", "Full-text search query"),
                                "filter", Map.of("type", "array", "items", Map.of("type", "string")),
                                "filterQuery", Map.of("type", "array", "items", Map.of("type", "string")),
                                "facet", Map.of("type", "array", "items", Map.of("type", "string")),
                                "limit", Map.of("type", "number", "default", 20),
                                "offset", Map.of("type", "number", "default", 0),
                                "sort", Map.of("type", "string"))))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            var searchBuilder = apiRoot.productProjections().search().get();

            if (args.containsKey("text")) {
                searchBuilder.addText("en", (String) args.get("text"));
            }
            if (args.containsKey("limit")) {
                searchBuilder.withLimit(((Number) args.get("limit")).intValue());
            }
            if (args.containsKey("offset")) {
                searchBuilder.withOffset(((Number) args.get("offset")).intValue());
            }
            if (args.containsKey("sort")) {
                searchBuilder.addSort((String) args.get("sort"));
            }

            ProductProjectionPagedSearchResponse response = searchBuilder
                    .executeBlocking()
                    .getBody();

            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("product_search_read", e);
        }
    }
}
