package com.tech.payload.annotations.validator;

import com.tech.payload.annotations.custom.SSN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SsnValidator implements ConstraintValidator<SSN, String> {

    private final String SSN_PATTERN = "^(?!0{3})(?!6{3})[0-8]\\d{2}-(?!0{2})\\d{2}-(?!0{4})\\d{4}$";

    @Override
    public boolean isValid(String ssn, ConstraintValidatorContext context) {
        if (ssn == null) return true;
        if (!ssn.matches("^[0-9-]+$")) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("SSN can only contain digits and ' - '")
                    .addConstraintViolation();
            return false;
        }

        return ssn.matches(SSN_PATTERN);
    }
}
