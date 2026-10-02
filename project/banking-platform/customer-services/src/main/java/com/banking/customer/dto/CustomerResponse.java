package com.banking.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerResponse {

    private String cif;
    private String name;
    private String email;
    private String mobile;
    private String kycStatus;
    private String status;
}