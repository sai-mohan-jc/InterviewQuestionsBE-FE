package com.banking.account.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleCustomerNotFound(
	        CustomerNotFoundException ex) {

	    Map<String, Object> response = new LinkedHashMap<>();

	    response.put("status", 404);
	    response.put("error", "CUSTOMER_NOT_FOUND");
	    response.put("message", ex.getMessage());

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(response);
	}
	
	@ExceptionHandler(CustomerServiceUnavailableException.class)
	public ResponseEntity<Map<String, Object>> handleCustomerServiceUnavailable(
	        CustomerServiceUnavailableException ex) {

	    Map<String, Object> response = new LinkedHashMap<>();

	    response.put("status", 503);
	    response.put("error", "CUSTOMER_SERVICE_UNAVAILABLE");
	    response.put("message", ex.getMessage());

	    return ResponseEntity
	            .status(HttpStatus.SERVICE_UNAVAILABLE)
	            .body(response);
	}

}
