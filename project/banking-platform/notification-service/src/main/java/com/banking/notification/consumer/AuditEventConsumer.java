package com.banking.notification.consumer;

import java.time.Duration;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.banking.notification.event.PaymentEvent;

import reactor.core.publisher.Mono;

@Service
public class AuditEventConsumer {

	@KafkaListener(topics = "payment-events-json", groupId = "audit-group")
	public Mono<Void> consume(PaymentEvent event) {

		return Mono.delay(Duration.ofSeconds(1)).doOnNext(value -> {
			System.out.println("Audit received!");
			System.out.println(" Audit Payment ID: " + event.paymentId());
			System.out.println("Audit Status: " + event.status());
			System.out.println("Audit Amount: " + event.amount());
		}).then();
	}
}