package com.commercetools.mcp.tools.product;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductCreateTool extends AbstractCommercetoolsTool {

    public ProductCreateTool(ProjectApiRoot apiRoot) {
        super(apiRoot);
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("products_create")
                .description("Create a new product in commercetools")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "productType", Map.of("type", "object", "description", "Product type reference (id or key)"),
                                "name", Map.of("type", "object", "description", "Localized product name"),
                                "slug", Map.of("type", "object", "description", "Localized product slug"),
                                "key", Map.of("type", "string"),
                                "description", Map.of("type", "object"),
                                "categories", Map.of("type", "array", "items", Map.of("type", "object")),
                                "masterVariant", Map.of("type", "object"),
                                "variants", Map.of("type", "array", "items", Map.of("type", "object"))
                        ),
                        "required", List.of("productType", "name", "slug")
                ))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            ProductDraft draft = objectMapper.convertValue(args, ProductDraft.class);
            
            Product product = apiRoot.products()
                    .post(draft)
                    .executeBlocking()
                    .getBody();

            return ToolResult.success(toJson(product));
        } catch (Exception e) {
            return handleError("products_create", e);
        }
    }
}
