package com.banking.audit.controller;

import java.util.concurrent.Callable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
public class AuditCallableController {

	@GetMapping("/callable")
	public Callable<String> processAudit() {

		System.out.println("Initial request thread: " + Thread.currentThread().getName());

		return () -> {

			System.out.println("Callable execution thread: " + Thread.currentThread().getName());

			Thread.sleep(3000);

			System.out.println("Callable completed on: " + Thread.currentThread().getName());

			return "Audit completed using Callable";
		};
	}
}
