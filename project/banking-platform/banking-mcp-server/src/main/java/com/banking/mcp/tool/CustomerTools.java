package com.banking.mcp.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CustomerTools {

    private final RestClient customerRestClient;

    public CustomerTools(RestClient.Builder builder) {
        this.customerRestClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }


@McpTool(description = "Get customer details using customer CIF")
public String getCustomerDetails(
        @McpToolParam(
            description = "Customer identification number, for example CIF10001",
            required = true
        )
        String cif,
        McpSyncRequestContext requestContext) {

    String authorization = (String) requestContext
            .transportContext()
            .get("authorization");

    if (authorization == null || authorization.isBlank()) {
        throw new IllegalStateException(
                "Authorization token is missing");
    }

    return customerRestClient.get()
            .uri("/api/customers/{cif}", cif)
            .header("Authorization", authorization)
            .retrieve()
            .body(String.class);
}

}
