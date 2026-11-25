package com.banking.audit.exception;

import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuditExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Audit provider temporarily unavailable");
	}

	@ExceptionHandler(TimeoutException.class)
	public ResponseEntity<String> handleTimeout(TimeoutException ex) {

		return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Audit provider request timed out");
	}
}