package com.banking.account.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.banking.account.exception.CustomerNotFoundException;
import com.banking.account.exception.CustomerServiceUnavailableException;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public boolean customerExists(String cif) {

        try {
            restClient.get()
                    .uri("/api/customers/{cif}", cif)
                    .retrieve()
                    .toBodilessEntity();

            return true;

        } catch (HttpClientErrorException.NotFound ex) {

            throw new CustomerNotFoundException(
                    "Customer not found: " + cif);

        } catch (HttpServerErrorException ex) {

            throw new CustomerServiceUnavailableException(
                    "Customer Service returned server error",
                    ex);

        } catch (RestClientException ex) {

            throw new CustomerServiceUnavailableException(
                    "Unable to connect to Customer Service",
                    ex);
        }
    }
}