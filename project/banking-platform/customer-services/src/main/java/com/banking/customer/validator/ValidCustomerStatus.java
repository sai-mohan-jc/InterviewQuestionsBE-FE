package com.banking.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomerStatusValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomerStatus {

    String message() default
            "Customer cannot be ACTIVE when KYC status is PENDING";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}		