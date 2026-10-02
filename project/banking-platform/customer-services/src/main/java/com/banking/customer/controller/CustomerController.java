package com.banking.customer.controller;

import com.banking.customer.dto.CustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.entity.Customer;
import com.banking.customer.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
	    this.customerService = customerService;
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/{cif}")
    public CustomerResponse getCustomer(@PathVariable String cif) {
        return customerService.getCustomerByCif(cif);
    }
}