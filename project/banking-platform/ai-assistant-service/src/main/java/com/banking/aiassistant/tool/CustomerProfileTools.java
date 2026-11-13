package com.banking.aiassistant.tool;

import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;

import com.banking.aiassistant.client.CustomerServiceClient;
import com.banking.aiassistant.dto.CustomerProfileResponse;

@Component
public class CustomerProfileTools {

	private final CustomerServiceClient customerServiceClient;

	public CustomerProfileTools(CustomerServiceClient customerServiceClient) {
		this.customerServiceClient = customerServiceClient;
	}

	@Tool(description = "Retrieve the authenticated customer's own banking profile, including name, CIF, KYC status and account status.")
	public CustomerProfileResponse getMyCustomerProfile(ToolContext toolContext) {
         System.out.println("Customer Tools Calling");
		Map<String, Object> context = toolContext.getContext();

		String cif = (String) context.get("customerCif");
		String accessToken = (String) context.get("accessToken");

		System.out.println("CIF :"+cif +" "+"accessToken :"+accessToken);
		if (cif == null || cif.isBlank() || accessToken == null || accessToken.isBlank()) {
			throw new AccessDeniedException("Authenticated customer context is missing");
		}

		return customerServiceClient.getCustomerProfile(cif, accessToken);
	}
}