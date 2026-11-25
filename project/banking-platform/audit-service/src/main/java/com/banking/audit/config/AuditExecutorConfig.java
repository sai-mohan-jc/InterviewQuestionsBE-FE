package com.banking.audit.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditExecutorConfig {

	@Bean
	public ExecutorService auditExecutor() {
		return Executors.newFixedThreadPool(2);
	}
}