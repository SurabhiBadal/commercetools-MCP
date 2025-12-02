package com.commercetools.mcp.tools.producttype;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import com.commercetools.api.models.product_type.ProductTypeDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductTypeCreateTool extends AbstractCommercetoolsTool {
    public ProductTypeCreateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_type_create")
                .description("Create a product type with attribute definitions")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "name", Map.of("type", "string"),
                        "description", Map.of("type", "string"),
                        "key", Map.of("type", "string"),
                        "attributes", Map.of("type", "array", "items", Map.of("type", "object"))
                ), "required", List.of("name")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            ProductTypeDraft draft = objectMapper.convertValue(args, ProductTypeDraft.class);
            ProductType result = apiRoot.productTypes().post(draft).executeBlocking().getBody();
            return ToolResult.success(toJson(result));
        } catch (Exception e) {
            return handleError("product_type_create", e);
        }
    }
}
