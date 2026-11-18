package com.banking.payment.orchestration.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

	private static final String TOPIC = "payment-events-json";

	private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishPaymentEvent(PaymentEvent event) {

		kafkaTemplate.send(TOPIC, event.paymentId(), event).whenComplete((result, exception) -> {

			if (exception != null) {
				System.err.println("Failed to publish payment event: " + exception.getMessage());
			} else {
				System.out.println("Payment event published. Partition: " + result.getRecordMetadata().partition()
						+ ", Offset: " + result.getRecordMetadata().offset());
			}
		});
	}
}