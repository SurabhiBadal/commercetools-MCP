package com.commercetools.mcp.tools.producttailoring;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_tailoring.ProductTailoring;
import com.commercetools.api.models.product_tailoring.ProductTailoringDraft;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductTailoringCreateTool extends AbstractCommercetoolsTool {
    public ProductTailoringCreateTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("product_tailoring_create")
                .description("Create product tailoring for store-specific customizations")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "product", Map.of("type", "object"),
                        "store", Map.of("type", "object"),
                        "name", Map.of("type", "object"),
                        "description", Map.of("type", "object")
                ), "required", List.of("product", "store")))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            ProductTailoringDraft draft = objectMapper.convertValue(args, ProductTailoringDraft.class);
            ProductTailoring result = apiRoot.productTailoring().post(draft).executeBlocking().getBody();
            return ToolResult.success(toJson(result));
        } catch (Exception e) {
            return handleError("product_tailoring_create", e);
        }
    }
}
