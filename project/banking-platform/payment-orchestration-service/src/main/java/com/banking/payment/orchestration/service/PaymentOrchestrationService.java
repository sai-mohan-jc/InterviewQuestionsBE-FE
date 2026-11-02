
package com.banking.payment.orchestration.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.banking.payment.orchestration.client.EarmarkingClient;
import com.banking.payment.orchestration.client.ScreeningClient;
import com.banking.payment.orchestration.client.TransactionClient;
import com.banking.payment.orchestration.dto.CreateReviewRequest;
import com.banking.payment.orchestration.dto.PaymentRequest;
import com.banking.payment.orchestration.dto.PaymentResponse;
import com.banking.payment.orchestration.dto.PaymentStatusResponse;
import com.banking.payment.orchestration.dto.ReviewCaseResponse;
import com.banking.payment.orchestration.dto.ReviewDecisionNotification;
import com.banking.payment.orchestration.dto.ScreeningRequest;
import com.banking.payment.orchestration.dto.ScreeningResponse;
import com.banking.payment.orchestration.entity.Payment;
import com.banking.payment.orchestration.entity.PaymentStatus;
import com.banking.payment.orchestration.repository.PaymentRepository;

@Service
public class PaymentOrchestrationService {

	private final ScreeningClient screeningClient;
	private final TransactionClient transactionClient;
	private final EarmarkingClient earmarkingClient;
	private final PaymentRepository paymentRepository;

	public PaymentOrchestrationService(ScreeningClient screeningClient, TransactionClient transactionClient,
			EarmarkingClient earmarkingClient, PaymentRepository paymentRepository) {

		this.screeningClient = screeningClient;
		this.transactionClient = transactionClient;
		this.earmarkingClient = earmarkingClient;
		this.paymentRepository = paymentRepository;
	}

	public PaymentResponse initiatePayment(PaymentRequest request) {

		String paymentId = UUID.randomUUID().toString();

		Payment payment = new Payment();
		payment.setPaymentId(paymentId);
		payment.setFromAccount(request.fromAccount());
		payment.setToAccount(request.toAccount());
		payment.setAmount(request.amount());
		payment.setBeneficiaryName(request.beneficiaryName());
		payment.setStatus(PaymentStatus.INITIATED);
		payment.setMessage("Payment initiated");

		paymentRepository.save(payment);

		try {
			payment.setStatus(PaymentStatus.SCREENING);
			payment.setMessage("Screening in progress");
			paymentRepository.save(payment);

			ScreeningResponse screening = screeningClient.screen(new ScreeningRequest(paymentId, request.fromAccount(),
					request.toAccount(), request.amount(), request.beneficiaryName()));

			return switch (screening.decision().toUpperCase()) {

			case "BLOCK" -> {
				payment.setStatus(PaymentStatus.REJECTED);
				payment.setMessage(screening.reason());
				paymentRepository.save(payment);

				yield new PaymentResponse(paymentId, "REJECTED", screening.reason());
			}

			case "REVIEW" -> {
				ReviewCaseResponse review = earmarkingClient.createReviewCase(new CreateReviewRequest(paymentId,
						request.fromAccount(), request.toAccount(), request.amount(), screening.reason()));

				payment.setStatus(PaymentStatus.REVIEW_PENDING);
				payment.setHoldId(review.holdId());
				payment.setMessage("Review case created. Hold ID: " + review.holdId() + "; review deadline: "
						+ review.reviewDeadline());
				paymentRepository.save(payment);

				yield new PaymentResponse(paymentId, "REVIEW_PENDING", payment.getMessage());
			}

			case "PASS" -> {
				payment.setStatus(PaymentStatus.PROCESSING);
				payment.setMessage("Transfer submitted to Transaction Service");
				paymentRepository.save(payment);

				transactionClient.executeTransfer(paymentId, request);

				yield new PaymentResponse(paymentId, "TRANSFER_SUBMITTED",
						"Transfer request submitted to Transaction Service.");
			}

			default -> throw new IllegalStateException("Unknown screening decision: " + screening.decision());
			};

		} catch (Exception exception) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setMessage("Payment processing failed: " + exception.getMessage());
			paymentRepository.save(payment);
			throw exception;
		}
	}

	public PaymentStatusResponse getPaymentStatus(String paymentId) {

		Payment payment = paymentRepository.findById(paymentId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found: " + paymentId));

		return new PaymentStatusResponse(payment.getPaymentId(), payment.getFromAccount(), payment.getToAccount(),
				payment.getAmount(), payment.getStatus(), payment.getHoldId(), payment.getMessage(),
				payment.getCreatedAt(), payment.getUpdatedAt());
	}

	public void handleReviewDecision(ReviewDecisionNotification notification) {

		Payment payment = paymentRepository.findById(notification.paymentId())
				.orElseThrow(() -> new IllegalArgumentException("Payment not found: " + notification.paymentId()));

		if (payment.getHoldId() == null || !payment.getHoldId().equals(notification.holdId())) {
			throw new IllegalStateException("Hold ID does not match the payment");
		}

		String decision = notification.status().toUpperCase();

		if ("REJECTED".equals(decision)) {

			if (payment.getStatus() == PaymentStatus.REJECTED) {
				return; // Duplicate notification: already handled.
			}

			if (payment.getStatus() != PaymentStatus.REVIEW_PENDING) {
				throw new IllegalStateException("Payment cannot be rejected from status: " + payment.getStatus());
			}

			payment.setStatus(PaymentStatus.REJECTED);
			payment.setMessage("Compliance review rejected the payment; hold released.");
			paymentRepository.save(payment);
			return;
		}

		if ("APPROVED".equals(decision)) {

			if (payment.getStatus() == PaymentStatus.PROCESSING || payment.getStatus() == PaymentStatus.COMPLETED) {
				return; // Do not submit an already-processing payment again.
			}

			if (payment.getStatus() != PaymentStatus.REVIEW_PENDING) {
				throw new IllegalStateException("Payment cannot be approved from status: " + payment.getStatus());
			}

			// Save before submitting the transfer.
			payment.setStatus(PaymentStatus.PROCESSING);
			payment.setMessage("Review approved; submitting transfer for execution.");
			paymentRepository.save(payment);

			try {
				PaymentRequest request = new PaymentRequest(payment.getFromAccount(), payment.getToAccount(),
						payment.getAmount(), payment.getBeneficiaryName());

				transactionClient.executeTransfer(payment.getPaymentId(), request);

				payment.setMessage("Review approved; transfer submitted to Transaction Service.");
				paymentRepository.save(payment);

			} catch (RuntimeException exception) {
				payment.setMessage("Transfer submission outcome requires reconciliation: " + exception.getMessage());
				paymentRepository.save(payment);
				throw exception;
			}

			return;
		}

		throw new IllegalArgumentException("Unsupported review decision: " + notification.status());
	}

}
