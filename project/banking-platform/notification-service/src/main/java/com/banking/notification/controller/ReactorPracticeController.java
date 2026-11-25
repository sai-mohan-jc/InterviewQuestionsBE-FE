package com.banking.notification.controller;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.notification.dto.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactor")
public class ReactorPracticeController {

	@GetMapping("/mono-just")
	public Mono<String> monoJust() throws InterruptedException {

		System.out.println("1. Controller method started");

		String message = createMessage();

		Mono<String> result = Mono.just(message).doOnSubscribe(
				subscription -> System.out.println("4. Subscription received: " + Thread.currentThread().getName()))

				.doOnNext(value -> System.out.println("5. onNext: " + value))

				.doOnSuccess(value -> System.out.println("6. Mono successfully completed"));

		System.out.println("3. Mono created, returning to WebFlux");

		return result;
	}

	@GetMapping("/mono-callable")
	public Mono<String> monoCallable() throws InterruptedException {

		System.out.println("1. Controller method started");

		String message = createMessage();

		Mono<String> result = Mono.fromCallable(() -> message).doOnSubscribe(
				subscription -> System.out.println("4. Subscription received: " + Thread.currentThread().getName()))

				.doOnNext(value -> System.out.println("5. onNext: " + value))

				.doOnSuccess(value -> System.out.println("6. Mono successfully completed"));

		System.out.println("3. Mono created, returning to WebFlux");

		return result;
	}

	@GetMapping("/mono-defer")
	public Mono<String> monoDefer() throws InterruptedException {

		System.out.println("1. Controller method started");

		Mono<String> result = Mono.defer(() -> {
			System.out.println("3. Inside defer: Publisher creation");

			if (10 == 10) {
				return Mono.just("Generated at: " + System.currentTimeMillis());
			}

			return Mono.just("Just for learning: " + System.currentTimeMillis());

		});

		System.out.println("2. Mono created, returning to WebFlux");

		result.subscribe();
		result.subscribe();

		return result.map(mess -> mess + " Test");
	}

	private String createMessage() throws InterruptedException {

		System.out.println("2. createMessage() executed");
		Thread.sleep(3000);
		return "Notification created successfully";
	}

	private Mono<Customer> getCustomer(String cif) {

		System.out.println("Fetching customer: " + cif);

		return Mono.just(new Customer(cif, "John"));
	}

	@GetMapping("/mono-flatmap")
	public Mono<String> monoFlatMap() {

		return Mono.just("CIF10001").flatMap(cif -> getCustomer(cif))
				.map(customer -> "Notification prepared for " + customer.getName());
	}
	
	@GetMapping("/flatmap-concurrent")
	public Flux<String> flatMapConcurrent() {

	    return Flux.just("Customer", "Account", "Transaction")
	            .flatMap(service -> callService(service));
	}
	
	private Mono<String> callService(String service) {

	    int delay = switch (service) {
	        case "Customer" -> 2000;
	        case "Account" -> 3000;
	        default -> 1000;
	    };

	    return Mono.delay(Duration.ofMillis(delay))
	            .map(value -> service + " response received");
	}
}