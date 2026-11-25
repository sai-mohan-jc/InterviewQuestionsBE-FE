
package com.banking.audit.controller;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.audit.service.AuditCounterService;
import com.banking.audit.service.AuditService;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

	private final AuditService auditService;
	private final ExecutorService auditExecutor;
	private final AuditCounterService auditCounterService;

	public AuditController(ExecutorService auditExecutor, AuditService auditService,
			AuditCounterService auditCounterService) {
		this.auditExecutor = auditExecutor;
		this.auditService = auditService;
		this.auditCounterService = auditCounterService;
	}

	@GetMapping("/process")
	public String processAudit1() {

		String threadName = Thread.currentThread().getName();

		System.out.println("Audit started: " + LocalDateTime.now());
		System.out.println("Thread: " + threadName);

		try {
			// Simulate external API taking 3 seconds
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Audit interrupted", e);
		}

		System.out.println("Audit completed: " + LocalDateTime.now());

		return "Audit completed successfully. Thread: " + threadName;
	}

	@GetMapping("/process-with-pool")
	public String processWithPool() throws Exception {

		System.out.println("Request thread: " + Thread.currentThread().getName());

		Future<String> future = auditExecutor.submit(() -> {

			String workerThread = Thread.currentThread().getName();

			System.out.println("Provider started: " + workerThread);

			Thread.sleep(3000);

			System.out.println("Provider completed: " + workerThread);

			return "Audit completed by " + workerThread;
		});

		return future.get();
	}

	@GetMapping("/process-async")
	public CompletableFuture<String> processAsync() {

		System.out.println("Request thread: " + Thread.currentThread().getName());

		return CompletableFuture.supplyAsync(() -> {

			String workerThread = Thread.currentThread().getName();

			System.out.println("Audit started: " + workerThread);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException(e);
			}

			System.out.println("Audit completed: " + workerThread);

			return "Audit completed by " + workerThread;

		}, auditExecutor);
	}

	@GetMapping("/process-spring-async")
	public CompletableFuture<String> processAudit() {
		System.out.println("Request thread: " + Thread.currentThread().getName());

		return auditService.processAudit().orTimeout(2, TimeUnit.SECONDS);
	}

	@GetMapping("/counter")
	public int testCounter() throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 10000; i++) {
			auditCounterService.increment();
		}

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("Count :" + auditCounterService.getCount());
		return auditCounterService.getCount();
	}

	@GetMapping("/platform-thread-test")
	public String platformThreadTest() throws Exception {

		long start = System.currentTimeMillis();

		try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

			for (int i = 0; i < 1000; i++) {
				executor.submit(() -> {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				});
			}
		}

		return "Platform Thread Time: " + (System.currentTimeMillis() - start) + " ms";
	}

	@GetMapping("/virtual-thread-test")
	public String virtualThreadTest() throws Exception {

		AtomicInteger completed = new AtomicInteger(0);

		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

			for (int i = 0; i < 1000; i++) {
				executor.submit(() -> {
					try {
						Thread.sleep(3000);
						completed.incrementAndGet();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				});
			}
		}

		return "Completed audit tasks: " + completed.get();
	}

}
