package com.banking.customer.validator;

import com.banking.customer.dto.CustomerRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerStatusValidator
        implements ConstraintValidator<ValidCustomerStatus, CustomerRequest> {

    @Override
    public boolean isValid(
            CustomerRequest request,
            ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        String kycStatus = request.getKycStatus();
        String status = request.getStatus();

        if ("PENDING".equalsIgnoreCase(kycStatus)
                && "ACTIVE".equalsIgnoreCase(status)) {
        	
        	context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    "Customer cannot be ACTIVE when KYC status is PENDING")
                    .addPropertyNode("status")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}