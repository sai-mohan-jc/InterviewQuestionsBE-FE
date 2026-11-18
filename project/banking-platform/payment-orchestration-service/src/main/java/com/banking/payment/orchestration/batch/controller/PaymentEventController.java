package com.banking.payment.orchestration.batch.controller;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.payment.orchestration.event.PaymentEvent;
import com.banking.payment.orchestration.event.PaymentEventProducer;

@RestController
@RequestMapping("/api/payment-events")
public class PaymentEventController {

    private final PaymentEventProducer producer;

    public PaymentEventController(PaymentEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> publishEvent(
            @RequestBody PaymentEvent request) {

        PaymentEvent event = new PaymentEvent(
                UUID.randomUUID().toString(),
                request.paymentId(),
                request.status(),
                request.amount(),
                LocalDateTime.now()
        );

        producer.publishPaymentEvent(event);

        return ResponseEntity.accepted()
                .body("Payment event submitted to Kafka");
    }
}