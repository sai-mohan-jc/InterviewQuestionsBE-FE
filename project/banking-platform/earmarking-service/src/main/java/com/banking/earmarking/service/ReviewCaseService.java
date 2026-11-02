package com.banking.earmarking.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.earmarking.client.AccountEarmarkClient;
import com.banking.earmarking.dto.CreateReviewRequest;
import com.banking.earmarking.dto.ReviewCaseResponse;
import com.banking.earmarking.entity.ReviewCase;
import com.banking.earmarking.entity.ReviewStatus;
import com.banking.earmarking.repository.ReviewCaseRepository;

@Service
public class ReviewCaseService {

	private final ReviewCaseRepository repository;
	private final AccountEarmarkClient accountEarmarkClient;
	private final long reviewPeriodMinutes;

	public ReviewCaseService(ReviewCaseRepository repository, AccountEarmarkClient accountEarmarkClient,
			@Value("${earmarking.review-period-minutes:30}") long reviewPeriodMinutes) {

		this.repository = repository;
		this.accountEarmarkClient = accountEarmarkClient;
		this.reviewPeriodMinutes = reviewPeriodMinutes;
	}

	@Transactional
	public ReviewCaseResponse createReviewCase(CreateReviewRequest request) {

		var existing = repository.findByPaymentId(request.paymentId());

		if (existing.isPresent()) {
			ReviewCase reviewCase = existing.get();

			if (!reviewCase.getFromAccount().equals(request.fromAccount())
					|| !reviewCase.getToAccount().equals(request.toAccount())
					|| reviewCase.getAmount().compareTo(request.amount()) != 0) {
				throw new IllegalStateException("Payment ID already exists with different details");
			}

			return toResponse(reviewCase);
		}

		String holdId = "HOLD-" + UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime deadline = now.plusMinutes(reviewPeriodMinutes);

		// Reserve funds first. If this fails, don't create a review case.
		accountEarmarkClient.reserve(request.fromAccount(), holdId, request.amount());

		ReviewCase reviewCase = ReviewCase.builder().paymentId(request.paymentId()).holdId(holdId)
				.fromAccount(request.fromAccount()).toAccount(request.toAccount()).amount(request.amount())
				.reason(request.reason()).status(ReviewStatus.PENDING).createdAt(now).reviewDeadline(deadline).build();

		return toResponse(repository.save(reviewCase));
	}

	private ReviewCaseResponse toResponse(ReviewCase reviewCase) {
		return new ReviewCaseResponse(reviewCase.getPaymentId(), reviewCase.getHoldId(), reviewCase.getFromAccount(),
				reviewCase.getToAccount(), reviewCase.getAmount(), reviewCase.getStatus(), reviewCase.getReason(),
				reviewCase.getReviewDeadline());
	}

	@Transactional
	public ReviewCaseResponse decideReview(String paymentId, ReviewStatus decision) {

		if (decision != ReviewStatus.APPROVED && decision != ReviewStatus.REJECTED) {
			throw new IllegalArgumentException("Decision must be APPROVED or REJECTED");
		}

		ReviewCase reviewCase = repository.findByPaymentId(paymentId)
				.orElseThrow(() -> new IllegalArgumentException("Review case not found: " + paymentId));

		if (reviewCase.getStatus() != ReviewStatus.PENDING && reviewCase.getStatus() != ReviewStatus.OVERDUE) {
			throw new IllegalStateException("Review case has already been decided: " + reviewCase.getStatus());
		}

		reviewCase.setStatus(decision);
		reviewCase.setResolvedAt(LocalDateTime.now());

		return toResponse(repository.save(reviewCase));
	}

}
