package com.commercetools.mcp.tools.project;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.project.Project;
import com.commercetools.mcp.model.ToolDefinition;
import com.commercetools.mcp.model.ToolResult;
import com.commercetools.mcp.tools.base.AbstractCommercetoolsTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProjectReadTool extends AbstractCommercetoolsTool {
    public ProjectReadTool(ProjectApiRoot apiRoot) { super(apiRoot); }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
                .name("project_read")
                .description("Read project information including currencies, languages, countries, and settings")
                .inputSchema(Map.of("type", "object", "properties", Map.of()))
                .build();
    }

    @Override
    public ToolResult execute(Map<String, Object> args) {
        try {
            Project project = apiRoot.get().executeBlocking().getBody();
            return ToolResult.success(toJson(project));
        } catch (Exception e) {
            return handleError("project_read", e);
        }
    }
}
