package com.banking.earmarking.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.earmarking.client.AccountEarmarkClient;
import com.banking.earmarking.client.ReviewDecisionNotifier;
import com.banking.earmarking.dto.CreateReviewRequest;
import com.banking.earmarking.dto.ReviewCaseResponse;
import com.banking.earmarking.dto.ReviewDecisionNotification;
import com.banking.earmarking.dto.ReviewDecisionRequest;
import com.banking.earmarking.service.ReviewCaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReviewCaseController {

	private final ReviewCaseService reviewCaseService;
	private final AccountEarmarkClient accountEarmarkClient;
	private final ReviewDecisionNotifier decisionNotifier;

	public ReviewCaseController(ReviewCaseService reviewCaseService, AccountEarmarkClient accountEarmarkClient,
			ReviewDecisionNotifier decisionNotifier) {
		this.reviewCaseService = reviewCaseService;
		this.accountEarmarkClient = accountEarmarkClient;
		this.decisionNotifier = decisionNotifier;
	}

	@PostMapping("/review-cases")
	public ReviewCaseResponse createReviewCase(@Valid @RequestBody CreateReviewRequest request) {
		return reviewCaseService.createReviewCase(request);
	}

	@PutMapping("/review-cases/{paymentId}/decision")
	public ReviewCaseResponse decideReview(@PathVariable String paymentId,
			@Valid @RequestBody ReviewDecisionRequest request) {

		ReviewCaseResponse review = reviewCaseService.decideReview(paymentId, request.decision());

		// Release the reserved funds before proceeding.
		accountEarmarkClient.release(review.fromAccount(), review.holdId());

		// Notify orchestration to update/resume the original payment.
		decisionNotifier.notifyOrchestration(
				new ReviewDecisionNotification(review.paymentId(), review.holdId(), review.status()));

		return review;
	}
}
