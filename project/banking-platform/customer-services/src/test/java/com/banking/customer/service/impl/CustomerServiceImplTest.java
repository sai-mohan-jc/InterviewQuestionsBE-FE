package com.banking.customer.service.impl;

import com.banking.customer.dto.CustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.entity.Customer;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.exception.ResourceNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomerSuccessfully() {

        CustomerRequest request = new CustomerRequest();
        request.setCif("CIF10001");
        request.setName("Sai");
        request.setEmail("sai@example.com");
        request.setMobile("9876543210");
        request.setKycStatus("VERIFIED");
        request.setStatus("ACTIVE");

        when(customerRepository.existsByCif("CIF10001"))
                .thenReturn(false);

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setCif("CIF10001");
        savedCustomer.setName("Sai");
        savedCustomer.setEmail("sai@example.com");
        savedCustomer.setMobile("9876543210");
        savedCustomer.setKycStatus("VERIFIED");
        savedCustomer.setStatus("ACTIVE");

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(savedCustomer);

        CustomerResponse response =
                customerService.createCustomer(request);

        assertNotNull(response);
        assertEquals("CIF10001", response.getCif());
        assertEquals("Sai", response.getName());

        verify(customerRepository).existsByCif("CIF10001");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyExists() {

        CustomerRequest request = new CustomerRequest();
        request.setCif("CIF10001");

        when(customerRepository.existsByCif("CIF10001"))
                .thenReturn(true);

        DuplicateCustomerException exception =
                assertThrows(
                        DuplicateCustomerException.class,
                        () -> customerService.createCustomer(request)
                );

        assertEquals(
                "Customer already exists: CIF10001",
                exception.getMessage()
        );

        verify(customerRepository).existsByCif("CIF10001");

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void shouldReturnCustomerWhenCifExists() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCif("CIF10001");
        customer.setName("Sai");
        customer.setEmail("sai@example.com");
        customer.setMobile("9876543210");
        customer.setKycStatus("VERIFIED");
        customer.setStatus("ACTIVE");

        when(customerRepository.findByCif("CIF10001"))
                .thenReturn(Optional.of(customer));

        CustomerResponse response =
                customerService.getCustomerByCif("CIF10001");

        assertNotNull(response);
        assertEquals("CIF10001", response.getCif());
        assertEquals("Sai", response.getName());

        verify(customerRepository)
                .findByCif("CIF10001");
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        when(customerRepository.findByCif("CIF99999"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> customerService.getCustomerByCif("CIF99999")
                );

        assertEquals(
                "Customer not found: CIF99999",
                exception.getMessage()
        );

        verify(customerRepository)
                .findByCif("CIF99999");
    }
}