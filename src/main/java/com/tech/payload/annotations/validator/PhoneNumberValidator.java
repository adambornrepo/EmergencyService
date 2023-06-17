package com.tech.payload.annotations.validator;

import com.tech.payload.annotations.custom.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private final String PHONE_NUMBER_PATTERN = "^\\+(\\d{1,4}-\\d{3}-\\d{3}-\\d{4})$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone.matches(PHONE_NUMBER_PATTERN);
    }
}
