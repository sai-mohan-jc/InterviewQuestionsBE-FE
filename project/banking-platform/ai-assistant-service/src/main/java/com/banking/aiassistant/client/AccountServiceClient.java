package com.banking.aiassistant.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.aiassistant.dto.AccountSummaryResponse;

@Component
public class AccountServiceClient {

    private final RestClient accountRestClient;

    public AccountServiceClient(
            @Qualifier("accountRestClient") RestClient accountRestClient) {
        this.accountRestClient = accountRestClient;
    }

    public List<AccountSummaryResponse> getMyAccounts(String accessToken) {

        return accountRestClient.get()
                .uri("/api/accounts/my-accounts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<
                        List<AccountSummaryResponse>>() {});
    }
}