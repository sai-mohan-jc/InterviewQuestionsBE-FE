package com.banking.notification.controller;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@GetMapping("/send")
	public Mono<String> sendNotification(@RequestParam String message) {

		System.out.println("Notification request received: " + Thread.currentThread().getName());

		return Mono.delay(Duration.ofSeconds(3)).map(value -> {
			System.out.println("Notification completed: " + Thread.currentThread().getName());

			return "Notification sent: " + message;
		});
	}

	@GetMapping("/mono-demo")
	public Mono<String> monoDemo() {

		System.out.println("1. Controller started");

		Mono<String> result = Mono.just("Notification created")
				.doOnNext(value -> System.out.println("3. Mono emitted: " + value));

		System.out.println("2. Mono created");

		return result;
	}

	@GetMapping("/mono-empty")
	public Mono<String> monoEmpty() {
		return Mono.empty();
	}

	@GetMapping("/mono-defer")
	public Mono<String> monoDefer() {

		System.out.println("Controller executed");

		return Mono.defer(() -> {
			System.out.println("Defer supplier executed");
			return Mono.just("Notification created");
		});
	}

	@GetMapping("/mono-delay")
	public Mono<String> monoDelay() {

		return Mono.delay(Duration.ofSeconds(3)).map(value -> "Notification sent after 3 seconds");
	}

}
