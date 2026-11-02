package com.banking.earmarking.client;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountEarmarkClient {

	private final RestClient restClient;

	public AccountEarmarkClient(RestClient.Builder builder) {
		this.restClient = builder.baseUrl("http://localhost:8082").build();
	}

	public ReserveFundsResponse reserve(String accountNumber, String holdId, BigDecimal amount) {

		return restClient.post().uri("/api/accounts/{accountNumber}/earmarks", accountNumber)
				.body(new ReserveFundsRequest(holdId, amount)).retrieve().body(ReserveFundsResponse.class);
	}

	public record ReserveFundsRequest(String holdId, BigDecimal amount) {
	}

	public record ReserveFundsResponse(String holdId, String accountNumber, BigDecimal amount, String status,
			BigDecimal ledgerBalance, BigDecimal reservedBalance, BigDecimal availableBalance) {
	}

	public void release(String accountNumber, String holdId) {
		restClient.post().uri("/api/accounts/{accountNumber}/earmarks/{holdId}/release", accountNumber, holdId)
				.retrieve().toBodilessEntity();
	}

}
