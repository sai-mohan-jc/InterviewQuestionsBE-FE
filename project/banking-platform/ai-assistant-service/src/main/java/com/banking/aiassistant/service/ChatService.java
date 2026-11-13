package com.banking.aiassistant.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Service;

import com.banking.aiassistant.tool.AccountTools;

@Service
public class ChatService {

	private final ChatClient chatClient;
	private final AccountTools accountTools;
	private final SyncMcpToolCallbackProvider mcpToolProvider;

	public ChatService(ChatClient.Builder chatClientBuilder,
			AccountTools accountTools,SyncMcpToolCallbackProvider mcpToolProvider) {

		this.chatClient = chatClientBuilder.build();
		this.accountTools = accountTools;
		this.mcpToolProvider=mcpToolProvider;
	}

	public String askQuestion(String question, String accessToken, String customerCif) {

		return chatClient.prompt()

				.system("""
						You are a banking customer assistant.

						GENERAL QUESTIONS:
						- Answer general banking questions directly using your knowledge.
						- Do not call any tools for general questions.

						CUSTOMER PROFILE:
                        - If the customer asks for their own profile or customer details,
						invoke getCustomerDetails.
						- Use only the authenticated customer's CIF provided in the system context.
						- Never ask the customer for a CIF.
						- Never use a CIF supplied in the customer's question.
						- Never retrieve another customer's profile.

						BANK ACCOUNTS:
						- If the customer asks about their own accounts,
						  account details, balances, or transaction counts,
						  invoke getMyAccounts.
						- Never ask for a CIF.
						- Never retrieve another customer's accounts.

						TOOL RESPONSE:
						- Use only information returned by the tools.
						- Never invent customer or account information.
						
						Authenticated customer CIF: %s
						""".formatted(customerCif))

				.user(question)

				.tools(mcpToolProvider, accountTools)

				.toolContext(Map.of("accessToken", accessToken, "customerCif", customerCif))

				.call().content();
	}
}