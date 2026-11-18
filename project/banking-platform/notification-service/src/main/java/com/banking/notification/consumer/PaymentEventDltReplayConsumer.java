package com.banking.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.banking.notification.event.PaymentEvent;

@Service
public class PaymentEventDltReplayConsumer {

	@KafkaListener(topics = "payment-events-dlt", groupId = "notification-dlt-replay-group", autoStartup = "${app.kafka.dlt-replay.enabled:false}")
	public void replay(PaymentEvent event) {

		System.out.println("DLT replay started!");

		System.out.println("Payment ID: " + event.paymentId());

		System.out.println("Status: " + event.status());

		System.out.println("Amount: " + event.amount());

		System.out.println("DLT event processed successfully!");
	}
}