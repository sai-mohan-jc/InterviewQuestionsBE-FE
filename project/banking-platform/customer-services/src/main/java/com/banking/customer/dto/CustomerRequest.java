package com.banking.customer.dto;

import com.banking.customer.validator.ValidCif;
import com.banking.customer.validator.ValidCustomerStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@ValidCustomerStatus
public class CustomerRequest {

    @NotBlank(message = "CIF is required")
    @ValidCif
    private String cif;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Mobile number must contain 10 digits"
    )
    private String mobile;

    @NotBlank(message = "KYC status is required")
    private String kycStatus;

    @NotBlank(message = "Status is required")
    private String status;
}