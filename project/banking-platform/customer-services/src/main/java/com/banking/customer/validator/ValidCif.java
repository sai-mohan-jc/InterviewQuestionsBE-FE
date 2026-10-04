package com.banking.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CifValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCif {

    String message() default "Invalid CIF format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}