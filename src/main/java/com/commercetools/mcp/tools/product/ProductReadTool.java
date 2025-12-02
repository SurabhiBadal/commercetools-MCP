package com.commercetools.mcp.tools.product;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductPagedQueryResponse;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductReadTool extends AbstractCommercetoolsTool {

    public ProductReadTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("products_read")
                .description("Query and retrieve product information from commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "id", Map.of("type", "string"),
                                "key", Map.of("type", "string"),
                                "where", Map.of("type", "string"),
                                "limit", Map.of("type", "number", "default", 20),
                                "offset", Map.of("type", "number", "default", 0),
                                "sort", Map.of("type", "string"),
                                "expand", Map.of("type", "array", "items", Map.of("type", "string"))
                        )
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            if (args.containsKey("id")) {
                Product product = apiRoot.products()
                        .withId((String) args.get("id"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(product));
            }

            if (args.containsKey("key")) {
                Product product = apiRoot.products()
                        .withKey((String) args.get("key"))
                        .get()
                        .executeBlocking()
                        .getBody();
                return ToolResult.success(toJson(product));
            }

            var queryBuilder = apiRoot.products().get();
            if (args.containsKey("where")) queryBuilder.withWhere((String) args.get("where"));
            if (args.containsKey("limit")) queryBuilder.withLimit(((Number) args.get("limit")).intValue());
            if (args.containsKey("offset")) queryBuilder.withOffset(((Number) args.get("offset")).intValue());
            if (args.containsKey("sort")) queryBuilder.withSort((String) args.get("sort"));

            ProductPagedQueryResponse response = queryBuilder.executeBlocking().getBody();
            return ToolResult.success(toJson(response));
        } catch (Exception e) {
            return handleError("products_read", e);
        }
    }
}
