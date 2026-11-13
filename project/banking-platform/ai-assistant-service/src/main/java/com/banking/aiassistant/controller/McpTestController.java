package com.banking.aiassistant.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class McpTestController {

	private final SyncMcpToolCallbackProvider mcpToolProvider;

	public McpTestController(SyncMcpToolCallbackProvider mcpToolProvider) {
		this.mcpToolProvider = mcpToolProvider;
	}

	@GetMapping("/api/ai/mcp/tools")
	public List<String> getMcpTools() {

		ToolCallback[] callbacks = mcpToolProvider.getToolCallbacks();

		return Arrays.stream(callbacks).map(callback -> callback.getToolDefinition().name()).toList();
	}
}