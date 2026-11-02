
package com.banking.payment.orchestration.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.payment.orchestration.dto.CreateReviewRequest;
import com.banking.payment.orchestration.dto.ReviewCaseResponse;

@Component
public class EarmarkingClient {

    private final RestClient restClient;

    public EarmarkingClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8086")
                .build();
    }

    public ReviewCaseResponse createReviewCase(
            CreateReviewRequest request) {

        return restClient.post()
                .uri("/api/review-cases")
                .body(request)
                .retrieve()
                .body(ReviewCaseResponse.class);
    }
}
