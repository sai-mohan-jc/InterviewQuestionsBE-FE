package com.banking.aiassistant.tool;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.banking.aiassistant.client.AccountServiceClient;
import com.banking.aiassistant.dto.AccountSummaryResponse;

@Component
public class AccountTools {

    private final AccountServiceClient accountServiceClient;

    public AccountTools(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
    }

    @Tool(description = """
            Retrieve the authenticated customer's own bank accounts.
            Returns account number, account balance and transaction count.
            Use this tool when the customer asks about their accounts,
            account details, or account balances.
            Never use it to retrieve another customer's accounts.
            """)
    public List<AccountSummaryResponse> getMyAccounts(
            ToolContext toolContext) {
    	System.out.println("Account Tools Calling");
        Map<String, Object> context = toolContext.getContext();

        String accessToken = (String) context.get("accessToken");
        String customerCif = (String) context.get("customerCif");

        if (accessToken == null || accessToken.isBlank()
                || customerCif == null || customerCif.isBlank()) {

            throw new AccessDeniedException(
                    "Authenticated customer context is missing");
        }

        return accountServiceClient.getMyAccounts(accessToken);
    }
}