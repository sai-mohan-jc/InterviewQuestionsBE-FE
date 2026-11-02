package com.banking.payment.orchestration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.payment.orchestration.dto.PaymentRequest;
import com.banking.payment.orchestration.dto.PaymentResponse;
import com.banking.payment.orchestration.dto.PaymentStatusResponse;
import com.banking.payment.orchestration.service.PaymentOrchestrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentOrchestrationService orchestrationService;

	public PaymentController(PaymentOrchestrationService orchestrationService) {
		this.orchestrationService = orchestrationService;
	}

	@PostMapping
	public PaymentResponse initiatePayment(@Valid @RequestBody PaymentRequest request) {
		return orchestrationService.initiatePayment(request);
	}

	@GetMapping("/{paymentId}")
	public PaymentStatusResponse getPaymentStatus(@PathVariable String paymentId) {

		return orchestrationService.getPaymentStatus(paymentId);
	}

}
