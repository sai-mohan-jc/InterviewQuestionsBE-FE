package com.banking.customer.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CifValidator
        implements ConstraintValidator<ValidCif, String> {

    @Override
    public boolean isValid(
            String cif,
            ConstraintValidatorContext context) {

        if (cif == null) {
            return true;
        }

        return cif.matches("CIF\\d{5}");
    }
}