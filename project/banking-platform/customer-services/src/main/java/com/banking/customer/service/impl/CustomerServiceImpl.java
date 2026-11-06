package com.banking.customer.service.impl;

import com.banking.customer.dto.CustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.entity.Customer;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.exception.ResourceNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
    	
    	if (customerRepository.existsByCif(request.getCif())) {
            throw new DuplicateCustomerException(
                    "Customer already exists: " + request.getCif());
        }

        Customer customer = new Customer();
        
        customer.setCif(request.getCif());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setKycStatus(request.getKycStatus());
        customer.setStatus(request.getStatus());

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getCif(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getMobile(),
                savedCustomer.getKycStatus(),
                savedCustomer.getStatus()
        );
    }
    
    public String getDefaultvalue() {
    	System.out.println("Call default method");
    	return "Default";
    }

    @Override
    public CustomerResponse getCustomerByCif(String cif) {

        // Get the authenticated user from Spring Security context
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new AccessDeniedException("Access denied");
        }

        // Check whether the authenticated user is an ADMIN
        boolean isAdmin = jwtAuthentication.getAuthorities().stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(authority.getAuthority()));

        // CUSTOMER can access only their own CIF
        if (!isAdmin) {
            String authenticatedCif =
                    jwtAuthentication.getToken().getClaimAsString("customerCif");

            if (authenticatedCif == null || !authenticatedCif.equals(cif)) {
                throw new AccessDeniedException(
                        "You are not authorized to access this customer");
            }
        }

        // Fetch customer only after ownership validation
        Customer customer = customerRepository.findByCif(cif)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + cif));

        return new CustomerResponse(
                customer.getCif(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobile(),
                customer.getKycStatus(),
                customer.getStatus()
        );
    }
}