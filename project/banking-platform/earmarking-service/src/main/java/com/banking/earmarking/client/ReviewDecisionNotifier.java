
package com.banking.earmarking.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.earmarking.dto.ReviewDecisionNotification;

@Component
public class ReviewDecisionNotifier {

	private final RestClient restClient;

	public ReviewDecisionNotifier(RestClient.Builder builder) {
		this.restClient = builder.baseUrl("http://localhost:8085").build();
	}

	public void notifyOrchestration(ReviewDecisionNotification notification) {

		restClient.post().uri("/api/internal/reviews/{paymentId}/decision", notification.paymentId()).body(notification)
				.retrieve().toBodilessEntity();
	}
}
