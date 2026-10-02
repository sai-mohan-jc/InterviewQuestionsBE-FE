package com.banking.customer.service.impl;

import com.banking.customer.dto.CustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.entity.Customer;
import com.banking.customer.exception.ResourceNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

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
    	
    	String name = customerRepository.findByCif(cif).map(c->c.getName()).orElse("Default");
    	System.out.println("name :"+name);
    	String name1 = customerRepository.findByCif(cif).map(c->c.getName()).orElse(getDefaultvalue());
    	System.out.println("name1 :"+name1);
    	String name2 = customerRepository.findByCif(cif).map(c->c.getName()).orElseGet(()->getDefaultvalue());
    	System.out.println("name2 :"+name2);

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