
package com.banking.transaction.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountClient {

    private final RestClient restClient;

    public AccountClient(
            @Qualifier("accountServiceRestClient")
            RestClient restClient) {
        this.restClient = restClient;
    }

    public void updateBalance(
            String accountNumber,
            BigDecimal amount,
            String operation) {

        restClient.put()
                .uri("/api/accounts/{accountNumber}/balance", accountNumber)
                .body(new BalanceUpdateRequest(amount, operation))
                .retrieve()
                .toBodilessEntity();
    }

    public record BalanceUpdateRequest(
            BigDecimal amount,
            String operation) {
    }
}
