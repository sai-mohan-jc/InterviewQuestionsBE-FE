package com.banking.transaction.client;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountClient {

	private final RestClient.Builder restClientBuilder;

	private RestClient restClient() {
		return restClientBuilder.baseUrl("http://localhost:8082").build();
	}

	public void updateBalance(String accountNumber, BigDecimal amount, String operation) {

		restClient().put().uri("/api/accounts/{accountNumber}/balance", accountNumber)
				.body(new BalanceUpdateRequest(amount, operation)).retrieve().toBodilessEntity();
	}

	public record BalanceUpdateRequest(BigDecimal amount, String operation) {
	}
}
