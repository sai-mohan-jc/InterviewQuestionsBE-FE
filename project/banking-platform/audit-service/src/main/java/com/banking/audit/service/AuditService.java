package com.banking.audit.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

	@Async("auditTaskExecutor")
	public CompletableFuture<String> processAudit() {

		String threadName = Thread.currentThread().getName();

		System.out.println("Audit started: " + threadName);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}

		System.out.println("Audit completed: " + threadName);

		return CompletableFuture.completedFuture("Audit completed by " + threadName);
	}
}