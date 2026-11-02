package com.banking.earmarking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.earmarking.entity.ReviewCase;
import com.banking.earmarking.entity.ReviewStatus;

public interface ReviewCaseRepository extends JpaRepository<ReviewCase, Long> {

	Optional<ReviewCase> findByPaymentId(String paymentId);

	Optional<ReviewCase> findByHoldId(String holdId);

	List<ReviewCase> findByStatusAndReviewDeadlineBefore(ReviewStatus status, LocalDateTime deadline);
}
