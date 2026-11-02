
package com.banking.payment.orchestration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.payment.orchestration.dto.ReviewDecisionNotification;
import com.banking.payment.orchestration.service.PaymentOrchestrationService;

@RestController
@RequestMapping("/api/internal/reviews")
public class InternalReviewController {

    private final PaymentOrchestrationService orchestrationService;

    public InternalReviewController(
            PaymentOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/{paymentId}/decision")
    public ResponseEntity<Void> handleReviewDecision(
            @PathVariable String paymentId,
            @RequestBody ReviewDecisionNotification notification) {

        if (!paymentId.equals(notification.paymentId())) {
            throw new IllegalArgumentException(
                    "Payment ID in path and body must match");
        }

        orchestrationService.handleReviewDecision(notification);

        return ResponseEntity.ok().build();
    }
}
