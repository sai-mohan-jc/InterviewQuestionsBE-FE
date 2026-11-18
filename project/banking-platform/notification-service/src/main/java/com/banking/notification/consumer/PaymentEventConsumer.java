package com.banking.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.banking.notification.event.PaymentEvent;

@Service
public class PaymentEventConsumer {

    @KafkaListener(
        topics = "payment-events-json",
        groupId = "notification-retry-group"
    )
    public void consume(PaymentEvent event) {

        System.out.println(
            "Processing payment: " + event.paymentId()
        );

        throw new RuntimeException(
            "Simulated notification service failure"
        );
    }
}